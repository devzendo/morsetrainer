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

class SpeedFreqPanel(prefs: MorseTrainerPrefs) extends JPanel {
    var speedEntry: JTextField = null
    var speedSlider: JSlider = null
    var useFarnsworth: JCheckBox = null
    var farnSpeedLabel: JLabel = null
    var farnSpeedEntry: JTextField = null
    var farnSpeedSlider: JSlider = null
    var freqEntry: JTextField = null
    var freqSlider: JSlider = null
    var testButton: JButton = null

    setup()

    def setup() {
        val bl = new BoxLayout(this, BoxLayout.Y_AXIS)
        setLayout(bl)

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
        add(speedEntryPanel)

        val speedSliderPanel = new JPanel()
        speedSliderPanel.add(new JLabel("5"))
        speedSlider = new JSlider(SwingConstants.HORIZONTAL, 5, 40, prefs.getWordsPerMinute)
        speedSliderPanel.add(speedSlider)
        speedSliderPanel.add(new JLabel("40"))
        add(speedSliderPanel)

        val vGapPanel1 = new JPanel()
        vGapPanel1.add(new JLabel(" "))
        add(vGapPanel1)

        val useFTPanel = new JPanel()
        useFarnsworth = new JCheckBox("Use Farnsworth timing?", prefs.usingFarnsworth)
        useFTPanel.add(useFarnsworth)
        add(useFTPanel)

        val farnEntryPanel = new JPanel()
        farnSpeedLabel = new JLabel("Farnsworth speed:")
        farnEntryPanel.add(farnSpeedLabel)
        farnSpeedEntry = new JTextField("" + prefs.getFarnsworthWordsPerMinute, 3)
        farnEntryPanel.add(farnSpeedEntry)
        farnEntryPanel.add(new JLabel("WPM"))
        add(farnEntryPanel)

        val farnSpeedSliderPanel = new JPanel()
        farnSpeedSliderPanel.add(new JLabel("5"))
        farnSpeedSlider = new JSlider(SwingConstants.HORIZONTAL, 5, 40, prefs.getFarnsworthWordsPerMinute)
        farnSpeedSliderPanel.add(farnSpeedSlider)
        farnSpeedSliderPanel.add(new JLabel("40"))
        add(farnSpeedSliderPanel)

        val vGapPanel2 = new JPanel()
        vGapPanel2.add(new JLabel(" "))
        add(vGapPanel2)

        val freqEntryPanel = new JPanel()
        freqEntryPanel.add(new JLabel("Tone Frequency:"))
        freqEntry = new JTextField("" + prefs.getPulseFrequencyHz, 4)
        freqEntryPanel.add(freqEntry)
        freqEntryPanel.add(new JLabel("Hz"))
        add(freqEntryPanel)

        val freqSliderPanel = new JPanel()
        freqSliderPanel.add(new JLabel("400"))
        freqSlider = new JSlider(SwingConstants.HORIZONTAL, 400, 600, prefs.getPulseFrequencyHz)
        freqSliderPanel.add(freqSlider)
        freqSliderPanel.add(new JLabel("600"))
        add(freqSliderPanel)

        val vGapPanel3 = new JPanel()
        vGapPanel3.add(new JLabel(" "))
        add(vGapPanel3)

        val buttonPanel = new JPanel()
        testButton = new JButton("Test")
        buttonPanel.add(testButton)
        add(buttonPanel)
    }

}
