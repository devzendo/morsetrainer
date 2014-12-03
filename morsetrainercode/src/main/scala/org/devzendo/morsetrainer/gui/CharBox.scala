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

import javax.swing.JToggleButton
import org.devzendo.morsetrainer.Morse._
import scala.Some
import java.awt.Color

class CharBox(letter: Char, detailFn: Option[MorseChar => String] = None, colourFn: Option[MorseChar => Color] = None) extends JToggleButton("")
{
    colourFn match {
        case Some(fn) => {
            putClientProperty("Quaqua.Button.style","colorWell") // the style that allows a colour.
            setBackground(fn(letter))
        }
        case None => // noop
    }

    val detailsString = detailFn match {
        case Some(fn) => "<br><small>" + fn(letter) + "</small>"
        case None => ""
    }
    val body = "" + letter + detailsString
    setText(String.format("<html><head></head><body><center>%s</center></body></html>", body))

    override def setSelected(selected: Boolean) {
        super.setSelected(selected)

        colourFn match {
            case Some(fn) => {
                val letterColour = fn(letter)
                val brighter = Color.LIGHT_GRAY
                val color = if (selected) letterColour else brighter
                setBackground(color)
            }
            case None => // noop
        }

    }
}
