/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2015 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.huxhorn.lilith.swing.preferences;

import de.huxhorn.lilith.swing.MainFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TroubleshootingPanel
	extends JPanel
{
	private static final long serialVersionUID = 5589305263321629687L;

	private final Logger logger = LoggerFactory.getLogger(TroubleshootingPanel.class);
	private PreferencesDialog preferencesDialog;

	public TroubleshootingPanel(PreferencesDialog preferencesDialog)
	{
		this.preferencesDialog = preferencesDialog;
		createUI();
	}

	private void createUI()
	{
		add(new JButton(new InitDetailsViewAction()));
		add(new JButton(new InitExampleConditionScriptsAction()));
		add(new JButton(new InitExampleClipboardFormatterScriptsAction()));
		add(new JButton(new DeleteAllLogsAction()));
		add(new JButton(new CopySystemPropertiesAction()));
		add(new JButton(new CopyThreadsAction()));
		add(new JButton(new GarbageCollectionAction()));
	}

	public class InitDetailsViewAction
		extends AbstractAction
	{
		private static final long serialVersionUID = 8374235720899930441L;

		public InitDetailsViewAction()
		{
			super("Reinitialize details view files.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			preferencesDialog.reinitializeDetailsViewFiles();
		}
	}

	public class InitExampleConditionScriptsAction
		extends AbstractAction
	{
		private static final long serialVersionUID = -4197531497673863904L;

		public InitExampleConditionScriptsAction()
		{
			super("Reinitialize example groovy conditions.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			preferencesDialog.reinitializeGroovyConditions();
		}
	}

	public class InitExampleClipboardFormatterScriptsAction
		extends AbstractAction
	{
		private static final long serialVersionUID = -4197531497673863904L;

		public InitExampleClipboardFormatterScriptsAction()
		{
			super("Reinitialize example groovy clipboard formatters.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			preferencesDialog.reinitializeGroovyClipboardFormatters();
		}
	}

	public class DeleteAllLogsAction
		extends AbstractAction
	{
		private static final long serialVersionUID = 5218712842261152334L;

		public DeleteAllLogsAction()
		{
			super("Delete *all* logs.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			preferencesDialog.deleteAllLogs();
		}
	}

	public class CopySystemPropertiesAction
		extends AbstractAction
	{
		private static final long serialVersionUID = -2375370123070284280L;

		public CopySystemPropertiesAction()
		{
			super("Copy properties");
			putValue(SHORT_DESCRIPTION, "Copy system properties to the clipboard.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			Properties props = System.getProperties();
			SortedMap<String, String> sortedProps = new TreeMap<>();
			Enumeration<?> keys = props.propertyNames();
			while(keys.hasMoreElements())
			{
				String current = (String) keys.nextElement();
				String value = props.getProperty(current);
				if("line.separator".equals(current))
				{
					value = value.replace("\n", "\\n");
					value = value.replace("\r", "\\r");
				}
				sortedProps.put(current, value);

			}
			StringBuilder builder = new StringBuilder();
			for(Map.Entry<String, String> current : sortedProps.entrySet())
			{
				builder.append(current.getKey()).append("=").append(current.getValue()).append("\n");
			}
			MainFrame.copyText(builder.toString());
		}
	}

	public class CopyThreadsAction
		extends AbstractAction
	{
		private static final long serialVersionUID = -2375370123070284280L;

		public CopyThreadsAction()
		{
			super("Copy threads");
			putValue(SHORT_DESCRIPTION, "Copy the stacktraces of all threads to the clipboard.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

			StringBuilder builder = new StringBuilder();
			Map<ThreadGroup, List<ThreadHolder>> threadGroupMapping = new Hashtable<>();
			List<ThreadHolder> nullList = new ArrayList<>();
			for(Map.Entry<Thread, StackTraceElement[]> current : allStackTraces.entrySet())
			{
				Thread key = current.getKey();
				StackTraceElement[] value = current.getValue();
				ThreadHolder holder = new ThreadHolder(key, value);
				ThreadGroup group = key.getThreadGroup();
				if(group == null)
				{
					nullList.add(holder);
				}
				else
				{
					List<ThreadHolder> list = threadGroupMapping.get(group);
					if(list == null)
					{
						list = new ArrayList<>();
						threadGroupMapping.put(group, list);
					}
					list.add(holder);
				}

			}

			ThreadGroup rootGroup = null;
			Map<ThreadGroup, List<ThreadGroup>> threadGroups = new Hashtable<>();

			for(Map.Entry<ThreadGroup, List<ThreadHolder>> current : threadGroupMapping.entrySet())
			{
				ThreadGroup key = current.getKey();

				ThreadGroup root = addGroup(key, threadGroups);

				if(rootGroup == null)
				{
					rootGroup = root;
				}
				else if(rootGroup != root)
				{
					if(logger.isErrorEnabled()) logger.error("root={}, rootGroup={}", root, rootGroup);
				}
			}

			if(rootGroup == null)
			{
				if(logger.isErrorEnabled()) logger.error("Couldn't resolve root ThreadGroup!");
				return;
			}

			appendGroup(0, builder, rootGroup, threadGroups, threadGroupMapping);

			if(nullList.size() > 0)
			{
				builder.append("no group:\n");
				for(ThreadHolder current : nullList)
				{
					appendThread(1, builder, current);
				}
			}

			MainFrame.copyText(builder.toString());
		}

		private void appendGroup(int indent, StringBuilder builder, ThreadGroup group, Map<ThreadGroup, List<ThreadGroup>> threadGroups, Map<ThreadGroup, List<ThreadHolder>> threadGroupMapping)
		{
			String indentStr = createIndent(indent);
			builder.append(indentStr).append("ThreadGroup[name='").append(group.getName()).append("'" + ", daemon=")
				.append(group.isDaemon()).append(", destroyed=").append(group.isDestroyed()).append(", maxPriority=")
				.append(group.getMaxPriority()).append("]\n");

			List<ThreadGroup> groups = threadGroups.get(group);
			if(groups != null && groups.size() > 0)
			{
				builder.append(indentStr).append("groups = {\n");
				Collections.sort(groups, new ThreadGroupComparator());
				for(ThreadGroup current : groups)
				{
					appendGroup(indent + 1, builder, current, threadGroups, threadGroupMapping);
				}

				builder.append(indentStr).append("}\n");
			}

			List<ThreadHolder> threads = threadGroupMapping.get(group);
			if(threads != null && threads.size() > 0)
			{
				builder.append(indentStr).append("threads = {\n");
				Collections.sort(threads);
				for(ThreadHolder current : threads)
				{
					appendThread(indent + 1, builder, current);
				}
				builder.append(indentStr).append("}\n");
			}
		}

		private void appendThread(int indent, StringBuilder builder, ThreadHolder threadHolder)
		{
			String indentStr = createIndent(indent);

			Thread t = threadHolder.getThread();
			StackTraceElement[] ste = threadHolder.getStackTraceElements();
			builder.append(indentStr).append("Thread[name=").append(t.getName()).append(", id=").append(t.getId())
				.append(", priority=").append(t.getPriority()).append(", state=").append(t.getState())
				.append(", daemon=").append(t.isDaemon()).append(", alive=").append(t.isAlive())
				.append(", interrupted=").append(t.isInterrupted()).append("]\n");
			appendStackTraceElements(indent + 1, builder, ste);
		}

		private void appendStackTraceElements(int indent, StringBuilder builder, StackTraceElement[] stackTraceElements)
		{
			String indentStr = createIndent(indent);

			for(StackTraceElement current : stackTraceElements)
			{
				builder.append(indentStr).append("at ").append(current).append("\n");
			}
		}

		private String createIndent(int indent)
		{
			StringBuilder result = new StringBuilder();
			for(int i = 0; i < indent; i++)
			{
				result.append("\t");
			}
			return result.toString();
		}

		private ThreadGroup addGroup(ThreadGroup group, Map<ThreadGroup, List<ThreadGroup>> threadGroups)
		{
			ThreadGroup parentGroup = group.getParent();
			if(parentGroup == null)
			{
				return group; // root
			}
			List<ThreadGroup> list = threadGroups.get(parentGroup);
			if(list == null)
			{
				list = new ArrayList<>();
				threadGroups.put(parentGroup, list);
			}
			if(!list.contains(group))
			{
				list.add(group);
			}
			return addGroup(parentGroup, threadGroups);
		}
	}

	private static class ThreadGroupComparator
		implements Comparator<ThreadGroup>
	{

		public int compare(ThreadGroup o1, ThreadGroup o2)
		{
			if(o1 == o2)
			{
				return 0;
			}
			if(o1 == null)
			{
				return -1;
			}
			if(o2 == null)
			{
				return 1;
			}
			String name = o1.getName();
			String otherName = o2.getName();
			//noinspection StringEquality
			if(name == otherName)
			{
				return 0;
			}
			if(name == null)
			{
				return -1;
			}
			if(otherName == null)
			{
				return 1;
			}
			return name.compareTo(otherName);
		}
	}

	private static class ThreadHolder
		implements Comparable<ThreadHolder>
	{
		private final Thread thread;
		private final StackTraceElement[] stackTraceElements;

		private ThreadHolder(Thread thread, StackTraceElement[] stackTraceElements)
		{
			this.thread = thread;
			this.stackTraceElements = stackTraceElements;
		}

		public Thread getThread()
		{
			return thread;
		}

		public StackTraceElement[] getStackTraceElements()
		{
			return stackTraceElements;
		}

		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			ThreadHolder that = (ThreadHolder) o;

			return !(thread != null ? !thread.equals(that.thread) : that.thread != null);

		}

		public int hashCode()
		{
			return (thread != null ? thread.hashCode() : 0);
		}

		@SuppressWarnings("NullableProblems")
		public int compareTo(ThreadHolder other)
		{
			if(other == null)
			{
				throw new NullPointerException("other must not be null!");
			}
			if(thread == other.thread)
			{
				return 0;
			}
			if(thread == null)
			{
				return -1;
			}
			if(other.thread == null)
			{
				return 1;
			}
			String name = thread.getName();
			String otherName = other.thread.getName();
			//noinspection StringEquality
			if(name == otherName)
			{
				return 0;
			}
			// thread name is never null
			return name.compareTo(otherName);
		}
	}

	public class GarbageCollectionAction
		extends AbstractAction
	{
		private static final long serialVersionUID = -4636919088257143096L;

		public GarbageCollectionAction()
		{
			super("Execute GC");
			putValue(SHORT_DESCRIPTION, "Execute garbage collection.");
		}

		public void actionPerformed(ActionEvent actionEvent)
		{
			System.gc();
		}
	}

}
