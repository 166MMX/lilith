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

import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.swing.LilithKeyStrokes;

import static de.huxhorn.lilith.services.clipboard.FormatterTools.resolveCallStack;

public class LoggingCallLocationFormatter
		implements ClipboardFormatter
{
	private static final long serialVersionUID = 1443730189004642289L;

	private static final String ACCELERATOR = LilithKeyStrokes.getKeyStroke(LilithKeyStrokes.COPY_CALL_LOCATION_ACTION).toString();

	public String getName()
	{
		return "Copy call location";
	}

	public String getDescription()
	{
		return "Copies the call location, i.e. the first element of the logging events call stack, to the clipboard.";
	}

	public String getAccelerator()
	{
		return ACCELERATOR;
	}

	public boolean isCompatible(Object object)
	{
		return resolveCallStack(object).map(it -> it[0] != null).orElse(false);
	}

	public String toString(Object object)
	{
		return resolveCallStack(object).map(LoggingCallLocationFormatter::toStringOrNull).orElse(null);
	}

	public boolean isNative()
	{
		return true;
	}

	private static String toStringOrNull(ExtendedStackTraceElement[] callStack)
	{
		if (callStack[0] != null)
		{
			return callStack[0].toString(true);
		}
		return null;
	}
}
