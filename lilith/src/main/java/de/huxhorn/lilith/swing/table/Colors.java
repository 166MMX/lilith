/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2008 Joern Huxhorn
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
package de.huxhorn.lilith.swing.table;

import java.awt.Color;

public class Colors
{
	private ColorScheme colorScheme;
	private boolean sticky;

	/**
	 * Not sticky.
	 * @param foreground foreground color
	 * @param background background color
	 */
	public Colors(Color foreground, Color background)
	{
		this(new ColorScheme(foreground, background), false);
	}

	public Colors(Color foreground, Color background, boolean sticky)
	{
		this(new ColorScheme(foreground, background), sticky);
	}

	public Colors(ColorScheme colorScheme, boolean sticky)
	{
		this.colorScheme = colorScheme;
		this.sticky=sticky;
	}

	public ColorScheme getColorScheme()
	{
		return colorScheme;
	}

	public boolean isSticky()
	{
		return sticky;
	}
}