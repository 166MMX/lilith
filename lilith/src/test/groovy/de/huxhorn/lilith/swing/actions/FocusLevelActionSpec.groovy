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

package de.huxhorn.lilith.swing.actions

import de.huxhorn.lilith.conditions.LevelCondition
import de.huxhorn.lilith.data.EventWrapperCorpus
import de.huxhorn.lilith.data.logging.LoggingEvent

class FocusLevelActionSpec extends AbstractFilterActionSpecBase {
	@Override
	FilterAction createAction() {
		return new FocusLevelAction(LoggingEvent.Level.INFO)
	}

	@Override
	Set<Integer> expectedEnabledIndices() {
		EventWrapperCorpus.matchAnyLoggingEventSet()
	}

	@Override
	List<String> expectedSearchStrings() {
		List<String> result = new ArrayList<>()
		expectedEnabledIndices().each {
			// returns always the level used during construction
			result.add('INFO')
		}
		return result
	}

	@Override
	Class expectedConditionClass() {
		return LevelCondition.class
	}
}