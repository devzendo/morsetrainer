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

package org.devzendo.morsetrainer.gui.dialogs

import java.awt._
import org.devzendo.commonapp.gui.{GUIUtils, CursorManager}
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import org.devzendo.commonapp.gui.dialog.snaildialog.AbstractSnailDialog
import org.devzendo.commonapp.prefs.Prefs
import javax.swing._
import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.ClipGeneratorHolder

object ToolsOptionsDialog {
    private val LOGGER = LoggerFactory.getLogger(classOf[ToolsOptionsDialog])
}

class ToolsOptionsDialog(mainFrame: Frame, cursorManager: CursorManager, prefs: MorseTrainerPrefs, clipGeneratorHolder: ClipGeneratorHolder) extends AbstractSnailDialog(mainFrame: Frame, cursorManager: CursorManager, "Options") with DialogTools {

    import ToolsOptionsDialog._

    def prefsChanged: Unit = {
        LOGGER.debug("prefs have changed")
        assert(!SwingUtilities.isEventDispatchThread)
        clipGeneratorHolder.setUsingFarnsworth(prefs.usingFarnsworth)
        clipGeneratorHolder.setPulseFrequency(prefs.getPulseFrequencyHz)
        clipGeneratorHolder.setWordsPerMinute(prefs.getWordsPerMinute)
        clipGeneratorHolder.setFarnsworthWordsPerMinute(prefs.getFarnsworthWordsPerMinute)
    }

    def createMainComponent() = {
        val tabbedPane = new JTabbedPane()
        tabbedPane.addTab("Speed & Tone", padded(new SpeedFreqPanel(prefs, prefsChanged)))
        tabbedPane.addTab("Lesson", padded(new LessonPanel(prefs, prefsChanged)))

        padded(tabbedPane)
    }



    def initialise() {}
}
