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

package de.huxhorn.lilith.swing.menu

import de.huxhorn.lilith.data.EventWrapperCorpus
import de.huxhorn.lilith.swing.ApplicationPreferences

class ExcludeMenuSpec extends AbstractFilterMenuSpecBase {
	@Override
	AbstractFilterMenu createMenu() {
		return new ExcludeMenu(Mock(ApplicationPreferences), false)
	}

	@Override
	Set<Integer> expectedEnabledIndices() {
		def result = []
		result.addAll(EventWrapperCorpus.matchAnyLoggingEventSet())
		result.addAll(EventWrapperCorpus.matchAnyAccessEventSet())
		result
	}

	@Override
	int expectedGetSelectedEventCalls() {
		19
	}

	def 'setConditionNames() does not explode'() {
		setup:
		ExcludeMenu menu = (ExcludeMenu) createMenu()

		expect:
		menu.setConditionNames(['foo', 'bar'])
		menu.setConditionNames([])
		menu.setConditionNames(null)
	}
}