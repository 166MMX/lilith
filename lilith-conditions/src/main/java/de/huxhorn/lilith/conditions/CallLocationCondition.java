/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2013 Joern Huxhorn
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
package de.huxhorn.lilith.conditions;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;

import java.io.ObjectStreamException;

public class CallLocationCondition
	implements LilithCondition, SearchStringCondition
{
	private static final long serialVersionUID = -3772942542557888560L;

	public static final String DESCRIPTION = "CallLocation";
	private static final String AT_PREFIX = "at ";

	private String searchString;
	private transient StackTraceElement stackTraceElement;

	public CallLocationCondition()
	{
		this(null);
	}

	public CallLocationCondition(String searchString)
	{
		setSearchString(searchString);
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
		if(searchString == null)
		{
			stackTraceElement = null;
			return;
		}
		String actualString = searchString.trim();
		if(actualString.startsWith(AT_PREFIX))
		{
			actualString = actualString.substring(AT_PREFIX.length());
		}

		ExtendedStackTraceElement extendedStackTraceElement = ExtendedStackTraceElement.parseStackTraceElement(actualString);
		if(extendedStackTraceElement != null)
		{
			stackTraceElement = extendedStackTraceElement.getStackTraceElement();
		}
		else
		{
			stackTraceElement = null;
		}
	}

	public String getSearchString()
	{
		return searchString;
	}

	public StackTraceElement getStackTraceElement()
	{
		return stackTraceElement;
	}

	public boolean isTrue(Object value)
	{
		if(stackTraceElement == null)
		{
			return false;
		}
		if(value instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) value;
			Object eventObj = wrapper.getEvent();
			if(eventObj instanceof LoggingEvent)
			{
				LoggingEvent event = (LoggingEvent) eventObj;

				ExtendedStackTraceElement[] callStack = event.getCallStack();
				if(callStack != null && callStack.length > 0)
				{
					ExtendedStackTraceElement extendedStackTraceElement=callStack[0];
					if(extendedStackTraceElement == null)
					{
						return false;
					}
					return stackTraceElement.equals(extendedStackTraceElement.getStackTraceElement());
				}
			}
		}
		return false;
	}

	public CallLocationCondition clone()
		throws CloneNotSupportedException
	{
		CallLocationCondition result = (CallLocationCondition) super.clone();
		result.setSearchString(searchString);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		CallLocationCondition that = (CallLocationCondition) o;

		return !(stackTraceElement != null ? !stackTraceElement.equals(that.stackTraceElement) : that.stackTraceElement != null);
	}

	@Override
	public int hashCode()
	{
		return stackTraceElement != null ? stackTraceElement.hashCode() : 0;
	}

	private Object readResolve()
		throws ObjectStreamException
	{
		setSearchString(searchString);
		return this;
	}

	public String getDescription()
	{
		return DESCRIPTION;
	}

	@Override
	public String toString()
	{
		return getDescription() + "(" + stackTraceElement + ")";
	}
}
