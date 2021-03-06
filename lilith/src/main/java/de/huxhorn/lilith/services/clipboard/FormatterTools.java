/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
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
package de.huxhorn.lilith.services.clipboard;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Message;
import de.huxhorn.lilith.data.logging.ThreadInfo;
import de.huxhorn.lilith.data.logging.ThrowableInfo;
import de.huxhorn.sulky.formatting.SafeString;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

class FormatterTools
{
	private FormatterTools()
	{
	}

	static
	{
		new FormatterTools(); // stfu, coverage
	}

	static Optional<AccessEvent> resolveAccessEvent(Object object)
	{
		return Optional.ofNullable(resolveAccessEventInternal(object));
	}

	static Optional<LoggingEvent> resolveLoggingEvent(Object object)
	{
		return Optional.ofNullable(resolveLoggingEventInternal(object));
	}

	static Optional<String> resolveFormattedMessage(Object object)
	{
		Message message = resolveMessageInternal(object);
		if (message != null)
		{
			String value = message.getMessage();
			if (!isNullOrEmpty(value))
			{
				return Optional.of(value);
			}
		}
		return Optional.empty();
	}

	static Optional<String> resolveMessagePattern(Object object)
	{
		Message message = resolveMessageInternal(object);
		if (message != null)
		{
			String value = message.getMessagePattern();
			if (!isNullOrEmpty(value))
			{
				return Optional.of(value);
			}
		}
		return Optional.empty();
	}

	static Optional<ExtendedStackTraceElement[]> resolveCallStack(Object object)
	{
		LoggingEvent value = resolveLoggingEventInternal(object);
		if (value != null)
		{
			ExtendedStackTraceElement[] callStack = value.getCallStack();
			if (!isNullOrEmpty(callStack))
			{
				return Optional.of(callStack);
			}
		}
		return Optional.empty();
	}

	static Optional<ThrowableInfo> resolveThrowableInfo(Object object)
	{
		LoggingEvent value = resolveLoggingEventInternal(object);
		if (value != null)
		{
			return Optional.ofNullable(value.getThrowable());
		}
		return Optional.empty();
	}

	static Optional<String> resolveThrowableInfoName(Object object)
	{
		LoggingEvent value = resolveLoggingEventInternal(object);
		if (value != null)
		{
			ThrowableInfo throwable = value.getThrowable();
			if (throwable != null)
			{
				String name = throwable.getName();
				if (!isNullOrEmpty(name))
				{
					return Optional.of(name);
				}
			}
		}
		return Optional.empty();
	}

	static Optional<String> resolveThreadName(Object object)
	{
		LoggingEvent value = resolveLoggingEventInternal(object);
		if (value != null)
		{
			ThreadInfo threadInfo = value.getThreadInfo();
			if (threadInfo != null)
			{
				String name = threadInfo.getName();
				if (!isNullOrEmpty(name))
				{
					return Optional.of(name);
				}
			}
		}
		return Optional.empty();
	}

	static Optional<String> resolveThreadGroupName(Object object)
	{
		LoggingEvent value = resolveLoggingEventInternal(object);
		if (value != null)
		{
			ThreadInfo threadInfo = value.getThreadInfo();
			if (threadInfo != null)
			{
				String name = threadInfo.getGroupName();
				if (!isNullOrEmpty(name))
				{
					return Optional.of(name);
				}
			}
		}
		return Optional.empty();
	}

	static boolean isNullOrEmpty(Object object)
	{
		if (object == null)
		{
			return true;
		}
		if (object instanceof String)
		{
			return ((String) object).length() == 0;
		}
		if (object instanceof Map)
		{
			return ((Map) object).isEmpty();
		}

		if (object instanceof Collection)
		{
			return ((Collection) object).isEmpty();
		}

		if (object instanceof Object[])
		{
			return ((Object[]) object).length == 0;
		}

		return false;
	}

	static String toStringOrNull(Object value)
	{
		if (isNullOrEmpty(value))
		{
			return null;
		}

		if (value instanceof String)
		{
			return (String) value;
		}

		if (value instanceof Map)
		{
			return toStringOrNullInternal((Map) value);
		}

		if (value instanceof Collection)
		{
			return toStringOrNullInternal((Collection) value);
		}

		return SafeString.toString(value,
				SafeString.StringWrapping.CONTAINED,
				SafeString.StringStyle.GROOVY,
				SafeString.MapStyle.GROOVY);
	}

	private static String toStringOrNullInternal(Map<?, ?> value)
	{
		if (!(value instanceof SortedMap))
		{
			if (!value.containsKey(null))
			{
				// replace original map with sorted map, if possible
				try
				{
					value = new TreeMap<>(value);
				}
				catch (ClassCastException ignore)
				{
					// if not comparable
				}
			}
		}
		return SafeString.toString(value,
				SafeString.StringWrapping.CONTAINED,
				SafeString.StringStyle.GROOVY,
				SafeString.MapStyle.GROOVY);
	}

	private static String toStringOrNullInternal(Collection<?> value)
	{
		if (value instanceof Set)
		{
			if (!(value instanceof SortedSet))
			{
				if (!value.contains(null))
				{
					// replace original set with sorted set, if possible
					try
					{
						value = new TreeSet<>(value);
					}
					catch (ClassCastException ignore)
					{
						// if not comparable
					}
				}
			}
		}

		return SafeString.toString(value,
				SafeString.StringWrapping.CONTAINED,
				SafeString.StringStyle.GROOVY,
				SafeString.MapStyle.GROOVY);
	}

	private static AccessEvent resolveAccessEventInternal(Object object)
	{
		if (object instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) object;
			if (wrapper.getEvent() != null)
			{
				Object eventObj = wrapper.getEvent();
				if (eventObj instanceof AccessEvent)
				{
					return (AccessEvent) eventObj;
				}
			}
		}
		return null;
	}

	private static LoggingEvent resolveLoggingEventInternal(Object object)
	{
		if (object instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) object;
			if (wrapper.getEvent() != null)
			{
				Object eventObj = wrapper.getEvent();
				if (eventObj instanceof LoggingEvent)
				{
					return (LoggingEvent) eventObj;
				}
			}
		}
		return null;
	}

	private static Message resolveMessageInternal(Object object)
	{
		LoggingEvent event = resolveLoggingEventInternal(object);
		if (event != null)
		{
			return event.getMessage();
		}
		return null;
	}
}
