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

import java.awt._
import org.devzendo.commonapp.gui.CursorManager
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs
import org.devzendo.commonapp.gui.dialog.snaildialog.AbstractSnailDialog
import org.devzendo.commonapp.prefs.Prefs
import javax.swing._

class ToolsOptionsDialog(mainFrame: Frame, cursorManager: CursorManager, prefs: MorseTrainerPrefs) extends AbstractSnailDialog(mainFrame: Frame, cursorManager: CursorManager, "Options") with DialogTools {

    // speed and frequency
    var speedEntry: JTextField = null
    var speedSlider: JSlider = null
    var useFarnsworth: JCheckBox = null
    var farnSpeedLabel: JLabel = null
    var farnSpeedEntry: JTextField = null
    var farnSpeedSlider: JSlider = null
    var freqEntry: JTextField = null
    var freqSlider: JSlider = null
    var testButton: JButton = null

    // lesson
    var newCharsCheckbox: JCheckBox = null
    var missedCharsCheckbox: JCheckBox = null
    var similarCharsCheckbox: JCheckBox = null
    var randomLengthCheckbox: JCheckBox = null
    var wordLengthLabel: JLabel = null
    var wordLengthEntry: JTextField = null
    var sessLengthEntry: JTextField = null
    var sessLengthSlider: JSlider = null

    def createMainComponent() = {
        val tabbedPane = new JTabbedPane()
        tabbedPane.addTab("Speed & Tone", padded(createSpeedFreqPanel()))
        tabbedPane.addTab("Lesson", padded(createLessonPanel()))

        padded(tabbedPane)
    }

    private def createSpeedFreqPanel(): JPanel = {
        val panel = new JPanel()
        val bl = new BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.setLayout(bl)

        // Speed: [ 18] wpm
        // 5 --o----------- 40
        //
        // [x] Use Farnsworth timing?
        // Farnsworth speed:  [ 12] wpm
        // 5 -o------------ 40
        //
        // Tone frequency:  [ 600] Hz
        // 400 ------o---- 600
        //
        //        (Test)

        val speedEntryPanel = new JPanel()
        speedEntryPanel.add(new JLabel("Speed:"))
        speedEntry = new JTextField("" + prefs.getWordsPerMinute, 3)
        speedEntryPanel.add(speedEntry)
        speedEntryPanel.add(new JLabel("WPM"))
        panel.add(speedEntryPanel)

        val speedSliderPanel = new JPanel()
        speedSliderPanel.add(new JLabel("5"))
        speedSlider = new JSlider(SwingConstants.HORIZONTAL, 5, 40, prefs.getWordsPerMinute)
        speedSliderPanel.add(speedSlider)
        speedSliderPanel.add(new JLabel("40"))
        panel.add(speedSliderPanel)

        val vGapPanel1 = new JPanel()
        vGapPanel1.add(new JLabel(" "))
        panel.add(vGapPanel1)

        val useFTPanel = new JPanel()
        useFarnsworth = new JCheckBox("Use Farnsworth timing?", prefs.usingFarnsworth)
        useFTPanel.add(useFarnsworth)
        panel.add(useFTPanel)
        
        val farnEntryPanel = new JPanel()
        farnSpeedLabel = new JLabel("Farnsworth speed:")
        farnEntryPanel.add(farnSpeedLabel)
        farnSpeedEntry = new JTextField("" + prefs.getFarnsworthWordsPerMinute, 3)
        farnEntryPanel.add(farnSpeedEntry)
        farnEntryPanel.add(new JLabel("WPM"))
        panel.add(farnEntryPanel)

        val farnSpeedSliderPanel = new JPanel()
        farnSpeedSliderPanel.add(new JLabel("5"))
        farnSpeedSlider = new JSlider(SwingConstants.HORIZONTAL, 5, 40, prefs.getFarnsworthWordsPerMinute)
        farnSpeedSliderPanel.add(farnSpeedSlider)
        farnSpeedSliderPanel.add(new JLabel("40"))
        panel.add(farnSpeedSliderPanel)

        val vGapPanel2 = new JPanel()
        vGapPanel2.add(new JLabel(" "))
        panel.add(vGapPanel2)

        val freqEntryPanel = new JPanel()
        freqEntryPanel.add(new JLabel("Tone Frequency:"))
        freqEntry = new JTextField("" + prefs.getPulseFrequencyHz, 4)
        freqEntryPanel.add(freqEntry)
        freqEntryPanel.add(new JLabel("Hz"))
        panel.add(freqEntryPanel)

        val freqSliderPanel = new JPanel()
        freqSliderPanel.add(new JLabel("400"))
        freqSlider = new JSlider(SwingConstants.HORIZONTAL, 400, 600, prefs.getPulseFrequencyHz)
        freqSliderPanel.add(freqSlider)
        freqSliderPanel.add(new JLabel("600"))
        panel.add(freqSliderPanel)

        val vGapPanel3 = new JPanel()
        vGapPanel3.add(new JLabel(" "))
        panel.add(vGapPanel3)

        val buttonPanel = new JPanel()
        testButton = new JButton("Test")
        buttonPanel.add(testButton)
        panel.add(buttonPanel)

        panel
    }

    private def createLessonPanel(): JPanel = {
        val panel = new JPanel()
        val bl = new BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.setLayout(bl)

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
        panel.add(pmfPanel)

        val newCharsPanel = new JPanel()
        newCharsCheckbox = new JCheckBox("New characters?", true) // TODO get from prefs
        newCharsPanel.add(newCharsCheckbox)
        panel.add(newCharsPanel)

        val missedCharsPanel = new JPanel()
        missedCharsCheckbox = new JCheckBox("Missed characters?", true) // TODO get from prefs
        missedCharsPanel.add(missedCharsCheckbox)
        panel.add(missedCharsPanel)

        val similarCharsPanel = new JPanel()
        similarCharsCheckbox = new JCheckBox("Characters similar to missed ones?", true) // TODO get from prefs
        similarCharsPanel.add(similarCharsCheckbox)
        panel.add(similarCharsPanel)

        val vGapPanel1 = new JPanel()
        vGapPanel1.add(new JLabel(" "))
        panel.add(vGapPanel1)

        val randomLengthPanel = new JPanel()
        randomLengthCheckbox = new JCheckBox("Words are of random length?", true) // TODO get from prefs
        randomLengthPanel.add(randomLengthCheckbox)
        panel.add(randomLengthPanel)

        val wordLengthEntryPanel = new JPanel()
        wordLengthLabel = new JLabel("Word length:")
        wordLengthEntryPanel.add(wordLengthLabel)
        wordLengthEntry = new JTextField(""  , 2) // TODO get from prefs
        wordLengthEntryPanel.add(wordLengthEntry)
        panel.add(wordLengthEntryPanel)

        val vGapPanel2 = new JPanel()
        vGapPanel2.add(new JLabel(" "))
        panel.add(vGapPanel2)

        val sessLengthEntryPanel = new JPanel()
        sessLengthEntryPanel.add(new JLabel("Session length:"))
        sessLengthEntry = new JTextField("", 3) // TODO get from prefs
        sessLengthEntryPanel.add(sessLengthEntry)
        sessLengthEntryPanel.add(new JLabel("mins"))
        panel.add(sessLengthEntryPanel)

        val sessLengthSliderPanel = new JPanel()
        sessLengthSliderPanel.add(new JLabel("1"))
        sessLengthSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 5) // TODO get from prefs
        sessLengthSliderPanel.add(sessLengthSlider)
        sessLengthSliderPanel.add(new JLabel("10"))
        panel.add(sessLengthSliderPanel)

        panel
    }

    def initialise() {}
}
