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
import java.awt.{Color, BorderLayout, FlowLayout}
import org.devzendo.morsetrainer.prefs.{RecognitionRate, MorseTrainerPrefs}
import java.awt.event.{ActionEvent, ActionListener}
import org.devzendo.morsetrainer.Worst
import org.devzendo.morsetrainer.Morse.MorseChar

class WorstTrainerPanel(prefs: MorseTrainerPrefs, startTraining: StartTraining) extends JPanel with PanelTools {
    setLayout(new BorderLayout())


    def characterDetailFn(ch: MorseChar): String = {
        val rates = prefs.getCharacterRecognitionRates
        val rate = rates.getOrElse(ch, RecognitionRate(0, 0))
        val perc = (rate.probability * 100.0).toInt
        "" + perc + "%"
    }

    def colourFn(ch: MorseChar): Color = {
        val probability = prefs.getCharacterRecognitionRates.getOrElse(ch, RecognitionRate(0, 0)).probability * 100.0
        if (probability > 95.0) {
            Color.decode("0x33FF33")
        } else if (probability > 80.0) {
            Color.decode("0x66FF66")
        } else if (probability > 70.0) {
            Color.decode("0x66FF99")
        } else if (probability > 60.0) {
            Color.decode("0xFFFF99")
        } else if (probability > 40.0) {
            Color.decode("0xFFCC33")
        } else if (probability > 20) {
            Color.decode("0xFF6633")
        } else {
            Color.decode("0xCC3300")
        }
    }

    val charactersPanel = new CharactersPanel(false, Some(characterDetailFn), Some(colourFn))


    val eastPanel = new RightHandControlsPanel()
    val startButton = new JButton("Start training")
    startButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent) {
            startTraining.start(Worst)
        }
    })

    eastPanel.setButton(startButton)
    add(eastPanel, BorderLayout.EAST)

    add(charactersPanel, BorderLayout.WEST)
    add(new JLabel("These are the characters you are worst at recognising."), BorderLayout.NORTH)

}
