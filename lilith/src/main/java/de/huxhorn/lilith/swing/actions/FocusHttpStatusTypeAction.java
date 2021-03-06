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
package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpStatusTypeCondition;
import de.huxhorn.lilith.data.access.HttpStatus;
import de.huxhorn.sulky.conditions.Condition;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class FocusHttpStatusTypeAction
		extends AbstractBasicFilterAction
{
	private static final long serialVersionUID = -285766419031200234L;

	private final HttpStatus.Type type;

	public FocusHttpStatusTypeAction(HttpStatus.Type type)
	{
		super(type.getRange(), false);
		this.type = type;
		putValue(Action.SHORT_DESCRIPTION, type.toString());
		viewContainerUpdated();
	}

	@Override
	public Condition resolveCondition(ActionEvent e)
	{
		if(!isEnabled())
		{
			return null;
		}
		return new HttpStatusTypeCondition(type.name());
	}
}
