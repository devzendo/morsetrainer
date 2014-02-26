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

package org.devzendo.morsetrainer.gui.actionlisteners

import org.devzendo.commonapp.gui.menu.actionlisteners.SnailActionListener
import org.devzendo.commonapp.gui.{GUIUtils, CursorManager}
import java.awt.event.ActionEvent
import java.awt.Frame
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import org.devzendo.morsetrainer.gui.dialogs.ToolsOptionsDialog
import org.devzendo.morsetrainer.{TextAsMorseReader, ClipGeneratorHolder}

class ToolsOptionsActionListener(mainFrame: Frame, cursorManager: CursorManager, prefs: MorseTrainerPrefs, clipGeneratorHolder: ClipGeneratorHolder, textAsMorseReader: TextAsMorseReader) extends SnailActionListener(cursorManager) {
    def actionPerformedSlowly(e: ActionEvent) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                val dialog = new ToolsOptionsDialog(mainFrame, cursorManager, prefs, clipGeneratorHolder, textAsMorseReader)
                dialog.postConstruct()
                dialog.pack()
                dialog.setLocationRelativeTo(mainFrame)
                dialog.setVisible(true)
            }
        })
    }
}
