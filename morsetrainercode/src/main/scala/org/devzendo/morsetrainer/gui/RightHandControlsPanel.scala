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

import javax.swing.{JLabel, JButton, BoxLayout, JPanel}
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import java.awt._
import scala.collection.mutable.ArrayBuffer

object RightHandControlsPanel {
    val width = 200
}

class RightHandControlsPanel extends JPanel with PanelTools {
    val activebuttons = ArrayBuffer[JButton]()

    setLayout(new GridLayout(CharactersPanel.rows, 1, CharactersPanel.gap, CharactersPanel.gap))

    val controlsPanel = new JPanel()
    controlsPanel.setPreferredSize(new Dimension(RightHandControlsPanel.width, CharactersPanel.square))
    controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS))
    add(controlsPanel) // 1

    add(new JLabel(" ")) // 2
    add(new JLabel(" ")) // 3
    add(new JLabel(" ")) // 4
    add(new JLabel(" ")) // 5
    add(new JLabel(" ")) // 6


    val startButton = new JButton("???")
    setButton(startButton) // 7

    def getContentsPanel: JPanel = controlsPanel

    def setButton(button: JButton) {
        remove(startButton)
        add(button)
        button.setPreferredSize(new Dimension(RightHandControlsPanel.width, CharactersPanel.square))
    }
}
