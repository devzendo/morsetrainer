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

import org.devzendo.morsetrainer.Morse._
import org.devzendo.morsetrainer.prefs.RecognitionRate
import org.devzendo.morsetrainer.ProbabilityMap.Probability
import org.slf4j.LoggerFactory

object SessionProbabilityMaps {
    private val LOGGER = LoggerFactory.getLogger(SessionProbabilityMaps.getClass)

    private val PROBABILITY_IF_NOT_FOUND: Double = 0.1

    def genNewCharsProbMap(kochLevel: Int): ProbabilityMap[MorseChar] = {
        val startSet = KochLevels.morseCharsForLevel(kochLevel)

        val levelStrings = KochLevels.morseCharsSubArrayForLevel(kochLevel)

        val level_index = levelStrings.zipWithIndex
        val expanded_level_index = for {
            p <- level_index
            c <- p._1
        } yield (c, p._2)

        val probabilityCharMap = expanded_level_index.map( { p: (MorseChar, Int) => (p._1, Math.max(PROBABILITY_IF_NOT_FOUND, p._2.toDouble / expanded_level_index.size.toDouble )) } ).toMap

        def newCharsHigher(ch: MorseChar) = probabilityCharMap.getOrElse(ch, PROBABILITY_IF_NOT_FOUND)

        new SetFnProbabilityMap[MorseChar](startSet, newCharsHigher)
    }

    def genWorstCharsProbMap(ratesForStartSet: Map[MorseChar, RecognitionRate]): ProbabilityMap[MorseChar] = {
        val startSet = ratesForStartSet.keySet

        val probabilityMap = ratesForStartSet.map( { p: (MorseChar, RecognitionRate) => {
            if (p._2.probability > 0.9) (p._1, 0.0) else (p._1, 1 - p._2.probability)
        } } )

        new SetFnProbabilityMap[MorseChar](startSet, probabilityMap.getOrElse(_, 0.0))
    }

    def genMissedCharsProbMap(ratesForStartSet: Map[MorseChar, RecognitionRate]): ProbabilityMap[MorseChar] = {
        val startSet = ratesForStartSet.keySet

        val probabilityMap = ratesForStartSet.map( { p: (MorseChar, RecognitionRate) => {
            val initial = ( p._1, Math.max(PROBABILITY_IF_NOT_FOUND, 1 - p._2.probability))
            // reduce probability of chars we know better than 90% to 50%
            if (initial._2 > 0.9) (p._1, 5.0) else initial
        } } )

        def missedCharsHigher(ch: MorseChar) = probabilityMap.getOrElse(ch, 0.1)

        new SetFnProbabilityMap[MorseChar](startSet, missedCharsHigher)
    }

    def genSimilarCharsProbMap(startSet: Set[MorseChar], ratesForStartSet: Map[MorseChar, RecognitionRate]): ProbabilityMap[MorseChar] = {
        def getSimilarSet(ch: MorseChar): Set[MorseChar] = {
            ch.toUpper match {
                case 'K' => Set('R', 'N', 'A')
                case 'M' => Set('I', 'T', 'O')
                case 'R' => Set('K', 'A', 'N')
                case 'S' => Set('O', 'I', 'H')
                case 'U' => Set('W', 'J', 'A', 'V')
                case 'A' => Set('N', 'I', 'M')
                case 'P' => Set('X', 'J', 'R', '1')
                case 'T' => Set('M', 'E')
                case 'L' => Set('F', 'P', 'R')
                case 'O' => Set('S', 'M', 'B', 'J')
                case 'W' => Set('U', 'A', 'J', 'G')
                case 'I' => Set('E', 'S', 'M')
                case '.' => Set('+')
                case 'N' => Set('A', 'I', 'M')
                case 'J' => Set('W', 'B', '1')
                case 'E' => Set('T', 'I')
                case 'F' => Set('L', 'G', 'R')
                case '0' => Set('O', '5', '9')
                case 'Y' => Set('C', 'K')
                case ',' => Set('?')
                case 'V' => Set('B', 'U', '4')
                case 'G' => Set('D', 'Z')
                case '5' => Set('0', 'H', '6')
                case '/' => Set('P', 'C')
                case 'Q' => Set('K')
                case '9' => Set('1', '8', '0')
                case 'Z' => Set('G', '?')
                case 'H' => Set('S', '5')
                case '3' => Set('7', '2', '4', 'J')
                case '8' => Set('9', '7', 'Z')
                case 'B' => Set('J', 'D')
                case '?' => Set(',', 'Z', 'P')
                case '4' => Set('6', '5', 'V')
                case '2' => Set('1', '3', 'J')
                case '7' => Set('3', '8', 'Z')
                case 'C' => Set('Y', 'K', 'R')
                case '1' => Set('2', '0')
                case 'D' => Set('G', 'B')
                case '6' => Set('B', '5')
                case 'X' => Set('P', '/')
                case '=' => Set('X', 'U')
                case '+' => Set('.')
                case _ => Set()
            }
        }

        val probabilityMap = scala.collection.mutable.Map[MorseChar, Probability]()

        // fill in initial probabilityMap
        ratesForStartSet.foreach( { p: (MorseChar, RecognitionRate) => {
            val initial = ( p._1, Math.max(PROBABILITY_IF_NOT_FOUND, 1 - p._2.probability))
            probabilityMap += initial
        } } )
        // increase probability of similars, capping at 1.0
//        LOGGER.debug("the initial probability map is " + probabilityMap)
        probabilityMap.foreach( { p: (MorseChar, Probability) => {
            val similars = getSimilarSet(p._1).filter(startSet.contains)
//            LOGGER.debug("similars of " + p._1 + " are " + similars)
            for (similar <- similars) {
                val initialSimilarProbability: Probability = probabilityMap(similar)
//                LOGGER.debug("similar " + similar + " has initial probability " + initialSimilarProbability)
                val newSimilarProbability: Double = initialSimilarProbability * 1.2
//                LOGGER.debug("similar " + similar + " has new probability " + newSimilarProbability)
                val replacement = (similar, Math.min(1.0, newSimilarProbability))
//                LOGGER.debug("similar " + similar + " being replaced with " + replacement)
                probabilityMap += replacement
            }
        } } )
//        LOGGER.debug("the probability map after adding similars is " + probabilityMap)

        def similarCharsHigher(ch: MorseChar) = probabilityMap.getOrElse(ch, 0.1)

        new SetFnProbabilityMap[MorseChar](startSet, similarCharsHigher)

        //        new MapBasedProbabilityMap[MorseChar](probabilityMap.toMap)
    }
}
