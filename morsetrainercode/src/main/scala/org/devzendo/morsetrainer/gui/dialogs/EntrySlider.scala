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

import javax.swing._
import java.awt.event.{FocusEvent, ActionEvent}
import javax.swing.event.ChangeEvent

import DialogTools._

class EntrySlider(min: Int, max: Int, width: Int, curr: Int, intro: String, units: String, changeFn: (Int => Unit)) extends JPanel {
    var valueLabel: JLabel = null
    var valueEntry: JTextField = null
    var valueSlider: JSlider = null
    var minLabel: JLabel = null
    var maxLabel: JLabel = null
    var unitsLabel: JLabel = null


    val bl = new BoxLayout(this, BoxLayout.Y_AXIS)
    setLayout(bl)

    val valueEntryPanel = new JPanel()
    valueLabel = new JLabel(intro + ":")
    valueEntryPanel.add(valueLabel)
    valueEntry = new JTextField("" + curr, width)
    valueEntryPanel.add(valueEntry)
    unitsLabel = new JLabel(units)
    valueEntryPanel.add(unitsLabel)
    add(valueEntryPanel)
    valueEntry.addActionListener(
        (_: ActionEvent) => {
            fieldChanged()
        }
    )
    valueEntry.addFocusListener(
        (event: FocusEvent) => {
            if (event.getID == FocusEvent.FOCUS_LOST) {
                fieldChanged()
            }
        }
    )

    val valueSliderPanel = new JPanel()
    minLabel = new JLabel("" + min)
    valueSliderPanel.add(minLabel)
    valueSlider = new JSlider(SwingConstants.HORIZONTAL, min, max, curr)
    valueSliderPanel.add(valueSlider)
    maxLabel = new JLabel("" + max)
    valueSliderPanel.add(maxLabel)
    add(valueSliderPanel)
    valueSlider.addChangeListener(
        (_: ChangeEvent) => {
            val value: Int = valueSlider.getValue
            valueEntry.setText("" + value)
            new SwingWorker[Unit, AnyRef]() {
                def doInBackground(): Unit = {
                    changeFn(value)
                }
            }.execute()
        }
    )

    private def resetEntryToSlider() {
        valueEntry.setText("" + valueSlider.getValue)
    }

    private def fieldChanged() {
        try {
            val value = Integer.parseInt(valueEntry.getText)
            if (value >= min && value <= max) {
                valueSlider.setValue(value)
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        changeFn(value)

                    }
                }.execute()
            } else {
                resetEntryToSlider()
            }
        } catch {
            case (nfe: NumberFormatException) => resetEntryToSlider()
        }
    }

    def enableAllComponents(enable: Boolean) {
        valueLabel.setEnabled(enable)
        valueEntry.setEnabled(enable)
        valueSlider.setEnabled(enable)
        minLabel.setEnabled(enable)
        maxLabel.setEnabled(enable)
        unitsLabel.setEnabled(enable)
    }
}
