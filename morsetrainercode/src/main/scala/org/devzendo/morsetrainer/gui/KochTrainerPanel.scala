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

import javax.swing._
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import java.awt.{FlowLayout, BorderLayout}
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.{KochLevels, Koch}

object KochTrainerPanel {
    private val LOGGER = LoggerFactory.getLogger(classOf[KochTrainerPanel])
}

class KochTrainerPanel(prefs: MorseTrainerPrefs, startTraining: StartTraining) extends JPanel with PanelTools {
    import KochTrainerPanel._

    setLayout(new BorderLayout())

    val charactersPanel = new CharactersPanel(false)

    add(charactersPanel, BorderLayout.WEST)
    add(new JLabel("Choose one of the levels to change which characters to test with."), BorderLayout.NORTH)



    val eastPanel = new RightHandControlsPanel()
    val eastContentsPanel = eastPanel.getContentsPanel

    val comboItems: Array[AnyRef] = {
        val zip: List[(String, Int)] = KochLevels.levels.toList.zip(2 to KochLevels.levels.length + 1)
        (zip map { tuple: (String, Int) => "" + tuple._2 + " - " + tuple._1 + "  "} ).toArray
    }
    val combo = new JComboBox(comboItems)
    combo.setMaximumRowCount(20)
    val rflowPanel = new JPanel(new FlowLayout())
    rflowPanel.add(new JLabel("Level:"))
    rflowPanel.add(combo)
    combo.addItemListener(new ItemListener(){
        def itemStateChanged(e: ItemEvent) {
            val newLevel = combo.getSelectedIndex
            LOGGER.info("New level " + (newLevel + 2) + " selected")
            prefs.setKochLevel(newLevel + 2)
            setCharPanelSelections()
        }
    })
    eastContentsPanel.add(rflowPanel)


    val startButton = new JButton("Start training")
    startButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent) {
            startTraining.start(Koch)
        }
    })

    eastPanel.setButton(startButton)
    add(eastPanel, BorderLayout.EAST)

    val level = prefs.getKochLevel // 2..42
    combo.setSelectedIndex(level - 2)

    setCharPanelSelections()

    def setCharPanelSelections() {
        val level = prefs.getKochLevel // 2..42
        charactersPanel.deselectAll()
        for (i <- 0 until level - 1) {
            val charsAtThisLevel = KochLevels.levels(i)
            for (ch <- charsAtThisLevel) {
                charactersPanel.select(ch)
            }
        }
    }

}
