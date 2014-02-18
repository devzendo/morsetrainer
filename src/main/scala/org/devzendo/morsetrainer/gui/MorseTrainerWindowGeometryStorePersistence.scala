/**
 * Copyright (C) 2008-2014 Matt Gumbley, DevZendo.org <http://devzendo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devzendo.morsetrainer.gui

import org.devzendo.commonapp.gui.WindowGeometryStorePersistence
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs


/**
 * An adapter between MorseTrainerPrefs and WindowGeometryStorePersistence.
 *
 * @author matt
 *
 */
class MorseTrainerWindowGeometryStorePersistence(morseTrainerPrefs: MorseTrainerPrefs) extends WindowGeometryStorePersistence {

    def getWindowGeometry(windowName: String): String = {
        morseTrainerPrefs.getWindowGeometry(windowName)
    }

    def setWindowGeometry(windowName: String, geometry: String): Unit = {
        morseTrainerPrefs.setWindowGeometry(windowName, geometry)
    }
}
