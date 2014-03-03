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
import java.awt._
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import org.devzendo.morsetrainer.SessionMarker

class SessionPanel(prefs: MorseTrainerPrefs, abandonTraining: AbandonTraining, sessionMarker: SessionMarker) extends JPanel with PanelTools {

    setLayout(new BorderLayout())

    // TODO need to know which type of training is in use to send the appropriate
    // string to the morse player and marker?

    val infoPanel = new JPanel()
    infoPanel.setLayout(new GridLayout(6, 1, CharactersPanel.gap, CharactersPanel.gap))

    // 1
    infoPanel.add(new JLabel("  "))

    // 2
    val topPanel = new JPanel()
    val cancelButton = new JButton("Abandon session")
    cancelButton.addActionListener(abandonTraining)
    cancelButton.setPreferredSize(new Dimension(RightHandControlsPanel.width, CharactersPanel.square))
    // TODO and stop playing morse, stop the marker
    val progress = new JProgressBar(SwingConstants.HORIZONTAL, 0, prefs.getSessionLength * 60)
    progress.setBorderPainted(true)
    progress.setValue(0)
    progress.setPreferredSize(new Dimension(250, 20))
    topPanel.add(cancelButton)
    topPanel.add(new JLabel("    Completion:"))
    topPanel.add(progress)
    infoPanel.add(topPanel)

    // 3
    val entryPanel = new JPanel()
    val entryBox = new JTextField("", 20)
    entryBox.setAlignmentX(Component.CENTER_ALIGNMENT)
    entryBox.setBorder(BorderFactory.createEtchedBorder())
    entryBox.setEnabled(false)
    entryBox.setFont(new Font("Monospaced", Font.BOLD, 48))
    entryPanel.add(entryBox)
    infoPanel.add(entryPanel)

    // 4
    val labelPanel = new JPanel()
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS))
    addCentredJLabel(labelPanel, new JLabel("Type the characters you hear. Use backspace to correct mistakes."))
    addCentredJLabel(labelPanel, new JLabel("Don't forget to type space between words!"))
    addCentredJLabel(labelPanel, new JLabel("Session length: " + prefs.getSessionLength + " mins"))
    val typeLabel = new JLabel(" ")
    addCentredJLabel(labelPanel, typeLabel)
    infoPanel.add(labelPanel)

    // 5
    val countdownPanel = new JPanel()
    countdownPanel.setLayout(new BoxLayout(countdownPanel, BoxLayout.Y_AXIS))
    val timerLabel = new JLabel("Training starts in 5 seconds!")
    timerLabel.setFont(timerLabel.getFont.deriveFont(20f))
    addCentredJLabel(countdownPanel, timerLabel)
    infoPanel.add(countdownPanel)

    add(infoPanel, BorderLayout.CENTER)

    private def addCentredJLabel(panel: JPanel, label: JLabel) {
        label.setAlignmentX(Component.CENTER_ALIGNMENT)
        panel.add(label, BorderLayout.CENTER)
    }

    def setSessionType(sessionType: SessionType) {
        typeLabel.setText("Training type: " + sessionType)
    }
}
