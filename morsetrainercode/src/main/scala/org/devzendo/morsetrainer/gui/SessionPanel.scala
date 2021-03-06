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
import org.devzendo.morsetrainer._
import org.devzendo.commonapp.gui.GUIUtils

class SessionPanel(prefs: MorseTrainerPrefs, abandonTraining: AbandonTraining, finishTraining: FinishTraining) extends JPanel with PanelTools with SessionView {

    setLayout(new BorderLayout())

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
    private val entryBoxFont = new Font("Monospaced", Font.BOLD, 48)
    entryBox.setFont(entryBoxFont)
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
    val timerLabel = new JLabel("Get Ready!")
    timerLabel.setFont(timerLabel.getFont.deriveFont(20f))
    addCentredJLabel(countdownPanel, timerLabel)
    infoPanel.add(countdownPanel)

    add(infoPanel, BorderLayout.CENTER)

    private def addCentredJLabel(panel: JPanel, label: JLabel) {
        label.setAlignmentX(Component.CENTER_ALIGNMENT)
        panel.add(label, BorderLayout.CENTER)
    }

    def setSessionType(sessionType: SessionType) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                typeLabel.setText("Training type: " + sessionType)
                cancelButton.setText("Abandon session")
                cancelButton.getActionListeners.foreach(cancelButton.removeActionListener)
                cancelButton.addActionListener(abandonTraining)
                progress.setValue(0)
            }
        })

    }


    def clearCountdown() {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                timerLabel.setText("")
            }
        })
    }

    def setCountdownSeconds(secs: Int) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                timerLabel.setText("Training starts in " + secs + " second" + (if (secs != 1) "s" else "") + "!")
            }
        })
    }

    def endOfSession() {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                timerLabel.setText("Session complete!")
                cancelButton.setText("View report")
                cancelButton.getActionListeners.foreach(cancelButton.removeActionListener)
                cancelButton.addActionListener(finishTraining)
            }
        })
    }

    def setSessionDurationSeconds(secs: Int) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                progress.setMinimum(0)
                progress.setMaximum(secs)
            }
        })
    }

    def setCurrentSessionProgressSeconds(secs: Int) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                progress.setValue(secs)
            }
        })
    }

    def setEnteredText(enteredText: String) {
        GUIUtils.invokeLaterOnEventThread(new Runnable() {
            def run() {
                // flickers nastily. ugh.
                entryBox.setText(enteredText)
                entryBox.setCaretPosition(enteredText.length)
            }
        })
    }
}
