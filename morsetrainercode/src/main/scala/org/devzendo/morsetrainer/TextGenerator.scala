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
import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.SessionProbabilityMaps._

object TextGenerator {
    private val LOGGER = LoggerFactory.getLogger(classOf[TextGenerator])
}

class TextGenerator(prefs: MorseTrainerPrefs, recognitionRatePersister: RecognitionRatePersister) extends Iterator[MorseChar] {

    var iterator: Iterator[MorseChar] = new Iterator[MorseChar] {
        def hasNext: Boolean = false
        def next(): MorseChar = 'a'
    }

    val generated = ArrayBuffer[MorseChar]()

    def start(sessionType: SessionType) {
        iterator = sessionType match {
            case Koch => new KochIterator(prefs, recognitionRatePersister)
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
}


trait EndlessMorseCharIterator extends Iterator[MorseChar] {
    final def hasNext: Boolean = true
}


object KochIterator {
    private val LOGGER = LoggerFactory.getLogger(classOf[KochIterator])
}
class KochIterator(prefs: MorseTrainerPrefs, recognitionRatePersister: RecognitionRatePersister) extends EndlessMorseCharIterator {
    import KochIterator._
    val kochLevel: Int = prefs.getKochLevel
    val startSet = KochLevels.morseCharsForLevel(kochLevel)
    val definite = new DefiniteProbabilityMap[MorseChar](startSet)
    val newCharsProbMap = if (prefs.newCharsMoreFrequently) genNewCharsProbMap(kochLevel) else definite
//    LOGGER.debug("newCharsProbMap: " + newCharsProbMap)
//    val probMap = generateProbabilityMap

    def generateProbabilityMap: ProbabilityMap[MorseChar] = {
        val ratesForStartSet = recognitionRatePersister.getRecognitionRates(startSet)
        //        LOGGER.debug("ratesForStartSet: " + ratesForStartSet)

        val missedCharsProbMap = if (prefs.missedCharsMoreFrequently) genMissedCharsProbMap(ratesForStartSet) else definite
        //        LOGGER.debug("missedCharsProbMap: " + missedCharsProbMap)

        val similarCharsProbMap = if (prefs.similarCharsMoreFrequently) genSimilarCharsProbMap(startSet, ratesForStartSet) else definite
        //        LOGGER.debug("similarCharsProbMap: " + similarCharsProbMap)

        val finalProbMap = newCharsProbMap * missedCharsProbMap * similarCharsProbMap
        //        LOGGER.debug("finalProbMap: " + finalProbMap)
        finalProbMap
    }

    def next(): MorseChar = {
        // TODO need to decide how to intersperse spaces to end words
        // TODO decide if these are too expensive to recompute for every MorseChar
        val startTime = System.currentTimeMillis()

        val probMap = generateProbabilityMap

        val chosen = probMap.getProbabilistically
        val endTime = System.currentTimeMillis()
        LOGGER.debug("KochIterator chooses " + chosen._1 + " with probability " + chosen._2 + " in " + (endTime - startTime) + " ms")
        chosen._1
    }
}

class FreestyleIterator extends EndlessMorseCharIterator {
    def next(): MorseChar = {
        'a'
    }
}

class WorstIterator extends EndlessMorseCharIterator {
    def next(): MorseChar = {
        'a'
    }
}
