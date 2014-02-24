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
import java.awt.event.ActionEvent
import DialogTools._
import org.slf4j.LoggerFactory

object SpeedFreqPanel {
    private val LOGGER = LoggerFactory.getLogger(classOf[SpeedFreqPanel])

    val MIN_SPEED = 5
    val MAX_SPEED = 40
    val MIN_FREQ = 400
    val MAX_FREQ = 600
}

class SpeedFreqPanel(prefs: MorseTrainerPrefs, prefsChangedNotifier: => Unit) extends JPanel with PanelTools {
    import SpeedFreqPanel._

    var farnSpeedEntrySlider: EntrySlider = null
    var useFarnsworth: JCheckBox = null
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

        add(new EntrySlider(MIN_SPEED, MAX_SPEED, 3, prefs.getWordsPerMinute, "Speed", "WPM",
            (speed: Int) => {
                LOGGER.debug("speed entry slider updated")
                prefs.setWordsPerMinute(speed)
                prefsChangedNotifier
            }))

        addVGap()

        val useFTPanel = new JPanel()
        useFarnsworth = new JCheckBox("Use Farnsworth timing?", prefs.usingFarnsworth)
        useFTPanel.add(useFarnsworth)
        add(useFTPanel)
        useFarnsworth.addActionListener(
            (_: ActionEvent) => {
              val value = useFarnsworth.isSelected
                farnSpeedEntrySlider.enableAllComponents(value)
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        prefs.setUsingFarnsworth(value)
                        prefsChangedNotifier
                    }
                }.execute()
            }
        )

        farnSpeedEntrySlider = new EntrySlider(MIN_SPEED, MAX_SPEED, 3, prefs.getFarnsworthWordsPerMinute, "Farnsworth speed", "WPM",
            (speed: Int) => {
                prefs.setFarnsworthWordsPerMinute(speed)
                prefsChangedNotifier
            })
        add(farnSpeedEntrySlider)

        farnSpeedEntrySlider.enableAllComponents(prefs.usingFarnsworth)

        addVGap()

        add(new EntrySlider(MIN_FREQ, MAX_FREQ, 4, prefs.getPulseFrequencyHz, "Tone frequency", "Hz",
            (freq: Int) => {
                prefs.setPulseFrequency(freq)
                prefsChangedNotifier
            }))

        addVGap()

        val buttonPanel = new JPanel()
        testButton = new JButton("Test")
        buttonPanel.add(testButton)
        add(buttonPanel)
    }
}
