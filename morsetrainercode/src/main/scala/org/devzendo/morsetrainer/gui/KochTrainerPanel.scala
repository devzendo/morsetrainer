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

import javax.swing.{BoxLayout, JComboBox, JLabel, JPanel}
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import java.awt.{FlowLayout, BorderLayout, Dimension}
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import org.devzendo.morsetrainer.TextToMorse
import java.awt.event.{ItemEvent, ItemListener}
import org.slf4j.LoggerFactory

object KochLevels {
    val levels = Array(
        "KM",
        "R",
        "S",
        "U",
        "A",
        "P",
        "T",
        "L",
        "O",
        "W",
        "I",
        ".",
        "N",
        "J",
        "E",
        "F",
        "0",
        "Y",
        ",",
        "V",
        "G",
        "5",
        "/",
        "Q",
        "9",
        "Z",
        "H",
        "3",
        "8",
        "B",
        "?",
        "4",
        "2",
        "7",
        "C",
        "1",
        "D",
        "6",
        "X",
        "=",
        "+"
    )
}

object KochTrainerPanel {
    private val LOGGER = LoggerFactory.getLogger(classOf[KochTrainerPanel])

}

class KochTrainerPanel(prefs: MorseTrainerPrefs) extends JPanel with PanelTools {
    import KochTrainerPanel._

    setLayout(new BorderLayout())

    val westPanel = new JPanel(new FlowLayout())
    val charactersPanel = new CharactersPanel(false)
    westPanel.add(charactersPanel)

    add(westPanel, BorderLayout.WEST)
    add(new JLabel("Choose one of the levels to change which characters to test with."), BorderLayout.NORTH)



    val eastPanel = new JPanel()
    val boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS)
    eastPanel.setLayout(boxLayout)

    val comboItems: Array[AnyRef] = {
        val zip: List[(String, Int)] = KochLevels.levels.toList.zip(2 to KochLevels.levels.length + 1)
        (zip map { tuple: (String, Int) => "" + tuple._2 + " - " + tuple._1} ).toArray
    }
    val combo = new JComboBox(comboItems)
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
    eastPanel.add(rflowPanel)
    add(eastPanel, BorderLayout.EAST)

    val level = prefs.getKochLevel // 2..42
    combo.setSelectedIndex(level - 2)

    setCharPanelSelections()

    def setCharPanelSelections() {
        val level = prefs.getKochLevel // 2..42
        charactersPanel.deselectAll
        for (i <- 0 until level - 1) {
            val charsAtThisLevel = KochLevels.levels(i)
            for (ch <- charsAtThisLevel) {
                charactersPanel.select(ch)
            }
        }
    }

}
