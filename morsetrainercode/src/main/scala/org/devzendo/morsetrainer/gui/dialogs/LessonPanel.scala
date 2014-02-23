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

class LessonPanel(prefs: MorseTrainerPrefs) extends JPanel {
    var newCharsCheckbox: JCheckBox = null
    var missedCharsCheckbox: JCheckBox = null
    var similarCharsCheckbox: JCheckBox = null
    var randomLengthCheckbox: JCheckBox = null
    var wordLengthLabel: JLabel = null
    var wordLengthEntry: JTextField = null
    var sessLengthEntry: JTextField = null
    var sessLengthSlider: JSlider = null

    setup()

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

        val newCharsPanel = new JPanel()
        newCharsCheckbox = new JCheckBox("New characters?", true) // TODO get from prefs
        newCharsPanel.add(newCharsCheckbox)
        add(newCharsPanel)

        val missedCharsPanel = new JPanel()
        missedCharsCheckbox = new JCheckBox("Missed characters?", true) // TODO get from prefs
        missedCharsPanel.add(missedCharsCheckbox)
        add(missedCharsPanel)

        val similarCharsPanel = new JPanel()
        similarCharsCheckbox = new JCheckBox("Characters similar to missed ones?", true) // TODO get from prefs
        similarCharsPanel.add(similarCharsCheckbox)
        add(similarCharsPanel)

        val vGapPanel1 = new JPanel()
        vGapPanel1.add(new JLabel(" "))
        add(vGapPanel1)

        val randomLengthPanel = new JPanel()
        randomLengthCheckbox = new JCheckBox("Words are of random length?", true) // TODO get from prefs
        randomLengthPanel.add(randomLengthCheckbox)
        add(randomLengthPanel)

        val wordLengthEntryPanel = new JPanel()
        wordLengthLabel = new JLabel("Word length:")
        wordLengthEntryPanel.add(wordLengthLabel)
        wordLengthEntry = new JTextField(""  , 2) // TODO get from prefs
        wordLengthEntryPanel.add(wordLengthEntry)
        add(wordLengthEntryPanel)

        val vGapPanel2 = new JPanel()
        vGapPanel2.add(new JLabel(" "))
        add(vGapPanel2)

        val sessLengthEntryPanel = new JPanel()
        sessLengthEntryPanel.add(new JLabel("Session length:"))
        sessLengthEntry = new JTextField("", 3) // TODO get from prefs
        sessLengthEntryPanel.add(sessLengthEntry)
        sessLengthEntryPanel.add(new JLabel("mins"))
        add(sessLengthEntryPanel)

        val sessLengthSliderPanel = new JPanel()
        sessLengthSliderPanel.add(new JLabel("1"))
        sessLengthSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 5) // TODO get from prefs
        sessLengthSliderPanel.add(sessLengthSlider)
        sessLengthSliderPanel.add(new JLabel("10"))
        add(sessLengthSliderPanel)
    }

}
