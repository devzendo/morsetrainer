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

import org.devzendo.morsetrainer.prefs.{RecognitionRate, MorseTrainerPrefs}
import javax.swing._
import org.devzendo.morsetrainer.gui.dialogs.PanelTools
import java.awt.{FlowLayout, Color, BorderLayout}
import java.awt.event.{ActionEvent, ActionListener}
import org.devzendo.morsetrainer.Morse.MorseChar
import org.devzendo.commonapp.gui.panel.HTMLPanel

class ReportPanel(prefs: MorseTrainerPrefs, mainPanel: CardLayoutMainPanel) extends JPanel with PanelTools {

    setLayout(new BorderLayout())

    val surroundingPanel = new JPanel()
    val htmlPanel = new HTMLPanel()
    htmlPanel.setFont(htmlPanel.getFont.deriveFont(10.0f))
    htmlPanel.setBackground(getBackground)
    htmlPanel.setForeground(Color.BLACK)
    surroundingPanel.add(htmlPanel)

    add(surroundingPanel, BorderLayout.CENTER)


    val returnButton = new JButton("Return to Training Modes")
    returnButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent) {
            mainPanel.switchToPanel("trainingModesPanel")
        }
    })

    val buttonPanel = new JPanel(new FlowLayout())
    buttonPanel.add(returnButton)

    add(buttonPanel, BorderLayout.SOUTH)

    refresh()

    def createHTMLText(bodyText: String) =
        String.format("<html><head><title>sample text</title></head><body>%s</body></html>", bodyText)


    def charname(ch: MorseChar): String = {
        ch match {
            case ' ' => "SPACE"
            case _ => ch.toUpper.toString
        }
    }

    def percent(r: RecognitionRate): String = {
        "%3.0f".format(r.probability * 100.0) + "%"
    }

    def rowColor(ind: Int): String = {
        val background = getBackground
        toHTMLColor(if ((ind & 0x01) == 1) background.darker() else background.brighter())
    }

    def toHTMLColor(color: Color): String = {
        "\"#%6X\"".format(color.getRGB & 0x00ffffff)
    }

    def refresh() {
        val rates = prefs.getCharacterRecognitionRates
        val chars = rates.keys.toList.sortBy( ch => rates(ch).probability ) // worst first
        val charIndices = chars.zipWithIndex
        val rateTexts = charIndices.map( (charIndex: (MorseChar, Int)) => {
                "<tr bgcolor=" + rowColor(charIndex._2) + ">  <td>%s</td>  <td align=\"center\">%s</td>  <td align=\"right\">%d/%d</td>  </tr>".format(
                    charname(charIndex._1), percent(rates(charIndex._1)), rates(charIndex._1).correct, rates(charIndex._1).sentInTotal)} )
        val headerColor = toHTMLColor(getBackground.darker())

        val str = createHTMLText(
            "<table style=\"font-size:10; font-family:SansSerif;\" cellpadding=\"0\" cellspacing=\"0\">" +
            "<tr bgcolor=" + headerColor + ">  <th>Letter</th>  <th> &nbsp;&nbsp;&nbsp; Recognition success % &nbsp;&nbsp;&nbsp; </th>  <th>Correct/Sent</th>  </tr>" +
            rateTexts.mkString("") + "</table>")
        htmlPanel.setHTMLText(str)
    }
}
