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

import org.devzendo.morsetrainer.prefs.{RecognitionRate, MorseTrainerPrefs, RecognitionRatePersister}
import org.devzendo.morsetrainer.Morse.MorseChar
import scala.collection.mutable.ArrayBuffer

class TextGenerator(prefs: MorseTrainerPrefs, recognitionRatePersister: RecognitionRatePersister) extends Iterator[MorseChar] {
    var iterator: Iterator[MorseChar] = new Iterator[MorseChar] {
        def hasNext: Boolean = false
        def next(): MorseChar = 'a'
    }

    val generated = ArrayBuffer[MorseChar]()

    def start(sessionType: SessionType) {
        iterator = sessionType match {
            case Koch => new KochIterator()
            case Freestyle => new FreestyleIterator()
            case Worst => new WorstIterator()
        }
        generated.clear()
    }

    def hasNext: Boolean = iterator.hasNext

    def next(): MorseChar = {
        val char = iterator.next()
        generated += char

        char
    }

    def getGeneratedString: String = generated.toString()

    private trait EndlessMorseCharIterator extends Iterator[MorseChar] {
        final def hasNext: Boolean = true
    }

    def genNewCharsProbMap(startSet: Set[MorseChar]): ProbabilityMap[MorseChar] = {
        null
    }

    def genMissedCharsProbMap(ratesForStartSet: Map[MorseChar, RecognitionRate]): ProbabilityMap[MorseChar] = {
        null
    }

    def genSimilarCharsProbMap(startSet: Set[MorseChar], ratesForStartSet: Map[MorseChar, RecognitionRate]): ProbabilityMap[MorseChar] = {
        null
    }

    private class KochIterator extends EndlessMorseCharIterator {
        val startSet = KochLevels.morseCharsForLevel(prefs.getKochLevel) + ' '
        val definite = new DefiniteProbabilityMap[MorseChar](startSet)
        val newCharsProbMap = if (prefs.newCharsMoreFrequently) genNewCharsProbMap(startSet) else definite

        def next(): MorseChar = {
            // TODO decide if these are too expensive to recompute for every MorseChar
            val ratesForStartSet = recognitionRatePersister.getRecognitionRates(startSet)
            val missedCharsProbMap = if (prefs.missedCharsMoreFrequently) genMissedCharsProbMap(ratesForStartSet) else definite
            val similarCharsProbMap = if (prefs.similarCharsMoreFrequently) genSimilarCharsProbMap(startSet, ratesForStartSet) else definite
            val finalProbMap = newCharsProbMap * missedCharsProbMap * similarCharsProbMap
//            val probabilities: Map[MorseChar, Double] = new
            // new chars more frequently
            // missed chars more frequently
            // similar to missed chars more frequently
            // sort this by
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
