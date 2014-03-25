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

package org.devzendo.morsetrainer.prefs

import org.devzendo.morsetrainer.Morse.MorseChar
import org.devzendo.morsetrainer.{Key, Backspace, KeyboardEvent, Morse}
import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory

object RecognitionRatePersister {
    private val LOGGER = LoggerFactory.getLogger(classOf[RecognitionRatePersister])
}

class RecognitionRatePersister(prefs: MorseTrainerPrefs) {
    import RecognitionRatePersister._

    initialise()

    var receivedKeys = ArrayBuffer[MorseChar]()
    val playedChars = ArrayBuffer[MorseChar]()

    def reset() {
        receivedKeys.clear()
        playedChars.clear()
    }

    def persist() {

    }

    def keyReceived(key: KeyboardEvent) {
        LOGGER.info("Received key: " + key)
        key match {
            case Backspace => {
                if (!receivedKeys.isEmpty) {
                    receivedKeys = receivedKeys.init
                }
            }
            case Key(ch: Char) =>
                receivedKeys += ch
        }
    }

    def charPlayed(ch: MorseChar) {
        LOGGER.info("Played: '" + ch + "'")
        playedChars += ch
    }

    def initialise() {
        val startMap = prefs.getCharacterRecognitionRates
        def charToCharPlusRecognitionRate(ch: MorseChar): (MorseChar, RecognitionRate) = {
            val rr = startMap.getOrElse(ch, RecognitionRate(0, 0))
            (ch, rr)
        }
        val initMap = Morse.chars.map(charToCharPlusRecognitionRate).toMap
        prefs.setCharacterRecognitionRates(initMap)
    }

    def getRecognitionRates(forChars: Set[MorseChar]): Map[MorseChar, RecognitionRate] = {
        val fullMap = prefs.getCharacterRecognitionRates
        forChars.map( { ch: MorseChar => (ch, fullMap(ch)) } ).toMap
    }
}
