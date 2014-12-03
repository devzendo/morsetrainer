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

import javax.swing.{BoxLayout, JLabel, SwingUtilities, JPanel}
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import java.awt.{Color, Dimension, GridLayout}
import org.devzendo.morsetrainer.Morse
import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.Morse.MorseChar
import org.devzendo.commonapp.gui.GUIUtils

object CharactersPanel {
    private val LOGGER = LoggerFactory.getLogger(classOf[CharactersPanel])
    val rows = 7
    val cols = 7
    val square = 60
    val gap = 10
}

class CharactersPanel(enableable: Boolean, detailFn: Option[MorseChar => String] = None, colourFn: Option[MorseChar => Color] = None) extends JPanel with PanelTools {
    import CharactersPanel._

    val chars = Morse.chars
    val charButtonMap = scala.collection.mutable.Map[Char, CharBox]()

    private val gridLayout: GridLayout = new GridLayout(rows, cols, gap, gap)
    setLayout(gridLayout)

    for (ch <- chars) {
        val upper: Char = ch.toUpper
        val box: CharBox = new CharBox(upper, detailFn, colourFn)
        charButtonMap += (upper -> box)

        if (!enableable) {
            box.setFocusable(false)
            box.getMouseListeners.foreach( box.removeMouseListener )
        }

        add(box)
    }

    setPreferredSize(new Dimension(rows * square, cols * square))

    def deselectAll() {
        LOGGER.debug("Deselecting all buttons")
        charButtonMap.values.foreach{
            (charBox: CharBox) => {
                charBox.setSelected(false)
            }
        }
    }

    def getSelectedMorseChars: Set[MorseChar] = {
        assert(SwingUtilities.isEventDispatchThread)

        def optionSelect(entry: (Char, CharBox)) = {
            if (entry._2.isSelected) {
                Some(entry._1)
            } else {
                None
            }
        }

        charButtonMap.map(optionSelect).flatten.toSet
    }

    def select(ch: Char) {
        val upper: Char = ch.toUpper
        charButtonMap.get(upper) match {
            case Some(charBox: CharBox) => {
                LOGGER.debug("Selecting button " + upper)
                charBox.setSelected(true)
            }
            case None => LOGGER.warn("There is no button " + upper + " to select")
        }
    }
}
