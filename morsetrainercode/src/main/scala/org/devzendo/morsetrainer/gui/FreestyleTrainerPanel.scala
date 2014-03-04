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

import javax.swing.{JButton, JLabel, JPanel}
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import java.awt.{FlowLayout, BorderLayout}
import java.awt.event.{ActionEvent, ActionListener}
import org.devzendo.morsetrainer.Freestyle

class FreestyleTrainerPanel(prefs: MorseTrainerPrefs, startTraining: StartTraining) extends JPanel with PanelTools {
    setLayout(new BorderLayout())

    val charactersPanel = new CharactersPanel(true)

    add(charactersPanel, BorderLayout.WEST)


    val eastPanel = new RightHandControlsPanel()
    val startButton = new JButton("Start training")
    startButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent) {
            startTraining.start(Freestyle)
        }
    })

    eastPanel.setButton(startButton)
    add(eastPanel, BorderLayout.EAST)

    add(new JLabel("Select the individual characters with which to test."), BorderLayout.NORTH)

}
