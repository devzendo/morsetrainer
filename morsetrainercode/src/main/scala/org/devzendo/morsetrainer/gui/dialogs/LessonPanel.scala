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

package org.devzendo.morsetrainer.gui.dialogs

import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import javax.swing._
import java.awt.event.{FocusEvent, ItemEvent, ActionEvent}
import DialogTools._
import org.devzendo.commonapp.gui.GUIUtils

object LessonPanel {
    val MIN_SESSION = 1
    val MAX_SESSION = 10

    val MIN_WORD_LENGTH = 2
    val MAX_WORD_LENGTH = 10
}

class LessonPanel(prefs: MorseTrainerPrefs, prefsChangedNotifier: => Unit) extends JPanel with PanelTools {
    import LessonPanel._

    var wordLengthLabel: JLabel = null
    var wordLengthEntry: JTextField = null
    var sessEntrySlider: EntrySlider = null
    var lastGoodWordLength = if (prefs.areWordsRandomLength) MIN_WORD_LENGTH else prefs.getNonRandomWordLength

    setup()

    def enableDisableWordLength(checked: Boolean) {
        wordLengthEntry.setEnabled(!checked)
        wordLengthLabel.setEnabled(!checked)
    }

    def setup() {
        val bl = new BoxLayout(this, BoxLayout.Y_AXIS)
        setLayout(bl)

        // Played more frequently:
        // [x] New characters?
        // [x] Missed characters?
        // [x] Characters similar to missed ones?
        //
        // [x] Words are of random length?
        // Fixed word length:    [ 5]
        //
        // Session length: [  5] mins
        // 1 ------o------- 10

        val pmfPanel = new JPanel()
        pmfPanel.add(new JLabel("Played more frequently:"))
        add(pmfPanel)

        addCheckbox("New characters?", prefs.newCharsMoreFrequently, (checked: Boolean) => {
            prefs.setNewCharsMoreFrequently(checked)
        })

        addCheckbox("Missed characters?", prefs.missedCharsMoreFrequently, (checked: Boolean) => {
            prefs.setMissedCharsMoreFrequently(checked)
        })

        addCheckbox("Characters similar to missed ones?", prefs.similarCharsMoreFrequently, (checked: Boolean) => {
            prefs.setSimilarCharsMoreFrequently(checked)
        })

        addVGap()

        addCheckbox("Words are of random length?", prefs.areWordsRandomLength, (random: Boolean) => {
            prefs.setWordsRandomLength(random)
            GUIUtils.invokeLaterOnEventThread(new Runnable() {
                def run() {
                    enableDisableWordLength(random)
                }
            })
        })

        val wordLengthEntryPanel = new JPanel()
        wordLengthLabel = new JLabel("Word length:")
        wordLengthEntryPanel.add(wordLengthLabel)
        wordLengthEntry = new JTextField("" + lastGoodWordLength , 2)
        wordLengthEntryPanel.add(wordLengthEntry)
        add(wordLengthEntryPanel)
        wordLengthEntry.addActionListener(
            (_: ActionEvent) => {
                fieldChanged()
            }
        )
        wordLengthEntry.addFocusListener(
            (event: FocusEvent) => {
                if (event.getID == FocusEvent.FOCUS_LOST) {
                    fieldChanged()
                }
            }
        )

        addVGap()

        add(new EntrySlider(MIN_SESSION, MAX_SESSION, 3, prefs.getSessionLength, "Session length", "mins",
            (len: Int) => {
                prefs.setSessionLength(len)
                prefsChangedNotifier
            }))

        enableDisableWordLength(prefs.areWordsRandomLength)
    }

    private def resetEntryToLastGood() {
        wordLengthEntry.setText("" + lastGoodWordLength)
    }

    private def fieldChanged() {
        try {
            val value = Integer.parseInt(wordLengthEntry.getText)
            if (value >= MIN_WORD_LENGTH && value <= MAX_WORD_LENGTH) {
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        lastGoodWordLength = value
                        prefs.setNonRandomWordLength(value)
                        prefsChangedNotifier
                    }
                }.execute()
            } else {
                resetEntryToLastGood()
            }
        } catch {
            case (nfe: NumberFormatException) => resetEntryToLastGood()
        }
    }

    private def addCheckbox(title: String, initial: Boolean, changeFn: (Boolean => Unit)) {
        val panel = new JPanel()
        val checkBox = new JCheckBox(title, initial)
        checkBox.addItemListener(
            (_: ItemEvent) => {
                val value = checkBox.isSelected
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        changeFn(value)
                        prefsChangedNotifier
                    }
                }.execute()
            }
        )
        panel.add(checkBox)
        add(panel)
    }
}
