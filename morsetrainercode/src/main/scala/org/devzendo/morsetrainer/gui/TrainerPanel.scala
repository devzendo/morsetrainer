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

import org.devzendo.morsetrainer.Morse._
import org.devzendo.morsetrainer.prefs.{MorseTrainerPrefs, RecognitionRate}
import java.awt.Color

trait TrainerPanel {

    def characterDetailFn(prefs: MorseTrainerPrefs)(ch: MorseChar): String = {
        val rates = prefs.getCharacterRecognitionRates
        val rate = rates.getOrElse(ch, RecognitionRate(0, 0))
        val perc = (rate.probability * 100.0).toInt
        "" + perc + "%"
    }

    def colourFn(prefs: MorseTrainerPrefs)(ch: MorseChar): Color = {
        val probability = prefs.getCharacterRecognitionRates.getOrElse(ch, RecognitionRate(0, 0)).probability * 100.0
        if (probability > 95.0) {
            Color.decode("0x33FF33")
        } else if (probability > 80.0) {
            Color.decode("0x66FF66")
        } else if (probability > 70.0) {
            Color.decode("0x66FF99")
        } else if (probability > 60.0) {
            Color.decode("0xFFFF99")
        } else if (probability > 40.0) {
            Color.decode("0xFFCC33")
        } else if (probability > 20) {
            Color.decode("0xFF6633")
        } else {
            Color.decode("0xCC3300")
        }
    }

}
