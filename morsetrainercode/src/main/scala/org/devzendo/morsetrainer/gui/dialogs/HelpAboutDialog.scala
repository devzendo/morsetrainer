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
import org.devzendo.commonapp.gui.CursorManager
import org.devzendo.commonapp.gui.dialog.snaildialog.AbstractSnailDialog
import javax.swing._

class HelpAboutDialog(mainFrame: Frame, cursorManager: CursorManager) extends AbstractSnailDialog(mainFrame: Frame, cursorManager: CursorManager, "About") with DialogTools {

    def createMainComponent() = {
        val panel = new JPanel()
        val bl = new BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.setLayout(bl)

        panel.add(new JLabel("Morse Trainer " + "v.1.0.0")) // TODO take from pom
        panel.add(new JLabel("Koch method & freestyle Morse Code trainer"))
        panel.add(new JLabel("http://devzendo.org/content/morsetrainer"))
        panel.add(new JLabel("(C) 2014 Matt Gumbley M0CUV"))
        padded(panel)
    }

    def initialise() {}
}

