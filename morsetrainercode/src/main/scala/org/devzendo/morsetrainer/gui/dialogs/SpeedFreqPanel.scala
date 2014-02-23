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
import java.awt.event.{ActionEvent, ActionListener}
import DialogTools._
import javax.swing.event.ChangeEvent

object SpeedFreqPanel {
    val MIN_SPEED = 5
    val MAX_SPEED = 40
    val MIN_FREQ = 400
    val MAX_FREQ = 600

}
class SpeedFreqPanel(prefs: MorseTrainerPrefs) extends JPanel {
    import SpeedFreqPanel._

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

    def resetSpeedEntryToSlider() {
        speedEntry.setText("" + speedSlider.getValue)
    }
    def resetFarnSpeedEntryToSlider() {
        farnSpeedEntry.setText("" + farnSpeedSlider.getValue)
    }
    def resetFreqEntryToSlider() {
        freqEntry.setText("" + freqSlider.getValue)
    }

    def enableDisableFarnsworth(enable: Boolean) {
        farnSpeedLabel.setEnabled(enable)
        farnSpeedEntry.setEnabled(enable)
        farnSpeedSlider.setEnabled(enable)
    }

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
        speedEntry.addActionListener(
            (_: ActionEvent) => {
                try {
                    val speed = Integer.parseInt(speedEntry.getText)
                    if (speed <= MIN_SPEED && speed >= MAX_SPEED) {
                        speedSlider.setValue(speed)
                        new SwingWorker[Unit, AnyRef]() {
                            def doInBackground(): Unit = {
                                prefs.setWordsPerMinute(speed)

                            }
                        }.execute()
                    } else {
                        resetSpeedEntryToSlider()
                    }
                } catch {
                    case (nfe: NumberFormatException) => resetSpeedEntryToSlider()
                }
            }
        )

        val speedSliderPanel = new JPanel()
        speedSliderPanel.add(new JLabel("" + MIN_SPEED))
        speedSlider = new JSlider(SwingConstants.HORIZONTAL, MIN_SPEED, MAX_SPEED, prefs.getWordsPerMinute)
        speedSliderPanel.add(speedSlider)
        speedSliderPanel.add(new JLabel("" + MAX_SPEED))
        add(speedSliderPanel)
        speedSlider.addChangeListener(
            (_: ChangeEvent) => {
                val speed: Int = speedSlider.getValue
                speedEntry.setText("" + speed)
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        prefs.setWordsPerMinute(speed)
                    }
                }.execute()
            }
        )

        val vGapPanel1 = new JPanel()
        vGapPanel1.add(new JLabel(" "))
        add(vGapPanel1)

        val useFTPanel = new JPanel()
        useFarnsworth = new JCheckBox("Use Farnsworth timing?", prefs.usingFarnsworth)
        useFTPanel.add(useFarnsworth)
        add(useFTPanel)
        useFarnsworth.addActionListener(
            (_: ActionEvent) => {
                enableDisableFarnsworth(useFarnsworth.isSelected)
            }
        )

        val farnEntryPanel = new JPanel()
        farnSpeedLabel = new JLabel("Farnsworth speed:")
        farnEntryPanel.add(farnSpeedLabel)
        farnSpeedEntry = new JTextField("" + prefs.getFarnsworthWordsPerMinute, 3)
        farnEntryPanel.add(farnSpeedEntry)
        farnEntryPanel.add(new JLabel("WPM"))
        add(farnEntryPanel)
        farnSpeedEntry.addActionListener(
            (_: ActionEvent) => {
                try {
                    val speed = Integer.parseInt(farnSpeedEntry.getText)
                    if (speed <= MIN_SPEED && speed >= MAX_SPEED) {
                        farnSpeedSlider.setValue(speed)
                        new SwingWorker[Unit, AnyRef]() {
                            def doInBackground(): Unit = {
                                prefs.setFarnsworthWordsPerMinute(speed)
                            }
                        }.execute()
                    } else {
                        resetFarnSpeedEntryToSlider()
                    }
                } catch {
                    case (nfe: NumberFormatException) => resetFarnSpeedEntryToSlider()
                }
            }
        )

        val farnSpeedSliderPanel = new JPanel()
        farnSpeedSliderPanel.add(new JLabel("5"))
        farnSpeedSlider = new JSlider(SwingConstants.HORIZONTAL, MIN_SPEED, MAX_SPEED, prefs.getFarnsworthWordsPerMinute)
        farnSpeedSliderPanel.add(farnSpeedSlider)
        farnSpeedSliderPanel.add(new JLabel("40"))
        add(farnSpeedSliderPanel)
        farnSpeedSlider.addChangeListener(
            (_: ChangeEvent) => {
                val speed: Int = farnSpeedSlider.getValue
                farnSpeedEntry.setText("" + speed)
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        prefs.setFarnsworthWordsPerMinute(speed)
                    }
                }.execute()
            }
        )

        enableDisableFarnsworth(prefs.usingFarnsworth)

        val vGapPanel2 = new JPanel()
        vGapPanel2.add(new JLabel(" "))
        add(vGapPanel2)

        val freqEntryPanel = new JPanel()
        freqEntryPanel.add(new JLabel("Tone Frequency:"))
        freqEntry = new JTextField("" + prefs.getPulseFrequencyHz, 4)
        freqEntryPanel.add(freqEntry)
        freqEntryPanel.add(new JLabel("Hz"))
        add(freqEntryPanel)
        freqEntry.addActionListener(
            (_: ActionEvent) => {
                try {
                    val freq = Integer.parseInt(freqEntry.getText)
                    if (freq <= MIN_FREQ && freq >= MAX_FREQ) {
                        freqSlider.setValue(freq)
                        new SwingWorker[Unit, AnyRef]() {
                            def doInBackground(): Unit = {
                                prefs.setPulseFrequency(freq)
                            }
                        }.execute()
                    } else {
                        resetFreqEntryToSlider()
                    }
                } catch {
                    case (nfe: NumberFormatException) => resetFreqEntryToSlider()
                }
            }
        )

        val freqSliderPanel = new JPanel()
        freqSliderPanel.add(new JLabel("" + MIN_FREQ))
        freqSlider = new JSlider(SwingConstants.HORIZONTAL, MIN_FREQ, MAX_FREQ, prefs.getPulseFrequencyHz)
        freqSliderPanel.add(freqSlider)
        freqSliderPanel.add(new JLabel("" + MAX_FREQ))
        add(freqSliderPanel)
        freqSlider.addChangeListener(
            (_: ChangeEvent) => {
                val freq: Int = freqSlider.getValue
                freqEntry.setText("" + freq)
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        prefs.setPulseFrequency(freq)
                    }
                }.execute()
            }
        )

        val vGapPanel3 = new JPanel()
        vGapPanel3.add(new JLabel(" "))
        add(vGapPanel3)

        val buttonPanel = new JPanel()
        testButton = new JButton("Test")
        buttonPanel.add(testButton)
        add(buttonPanel)
    }

}
