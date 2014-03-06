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

package org.devzendo.morsetrainer

import org.devzendo.morsetrainer.prefs.{MorseTrainerPrefs, RecognitionRatePersister}
import org.devzendo.morsetrainer.Morse.MorseChar

class TextGenerator(prefs: MorseTrainerPrefs, recognitionRatePersister: RecognitionRatePersister) extends Iterator[MorseChar] {
    var iterator: Iterator[MorseChar] = new Iterator[MorseChar] {
        def hasNext: Boolean = false
        def next(): MorseChar = 'a'
    }

    def start(sessionType: SessionType) {
        iterator = sessionType match {
            case Koch => new KochIterator()
            case Freestyle => new FreestyleIterator()
            case Worst => new WorstIterator()
        }
    }

    def hasNext: Boolean = iterator.hasNext

    def next(): MorseChar = iterator.next()

    private trait EndlessMorseCharIterator extends Iterator[MorseChar] {
        final def hasNext: Boolean = true
    }

    private class KochIterator extends EndlessMorseCharIterator {
        def next(): MorseChar = {
            'a'
        }
    }
    private class FreestyleIterator extends EndlessMorseCharIterator {
        def next(): MorseChar = {
            'a'
        }
    }
    private class WorstIterator extends EndlessMorseCharIterator {
        def next(): MorseChar = {
            'a'
        }
    }
}
