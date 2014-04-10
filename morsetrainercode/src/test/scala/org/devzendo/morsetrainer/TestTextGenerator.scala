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

import org.scalatest.junit.{MustMatchersForJUnit, AssertionsForJUnit}
import org.easymock.EasyMock
import org.devzendo.morsetrainer.prefs.{RecognitionRate, RecognitionRatePersister, MorseTrainerPrefs}
import org.junit.Test
import org.devzendo.morsetrainer.Morse._
import org.slf4j.LoggerFactory
import scala.collection.mutable.ArrayBuffer

object TestTextGenerator {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestTextGenerator])
}

class TestTextGenerator extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestTextGenerator._

    @Test
    def testKochGenerator() {
        val mockPrefs = EasyMock.createMock(classOf[MorseTrainerPrefs])

        mockPrefs.getKochLevel
        EasyMock.expectLastCall().andReturn(12)

        mockPrefs.getCharacterRecognitionRates
        val rates: Map[MorseChar, RecognitionRate] = Map(
            'K' -> RecognitionRate(1, 10),
            'M' -> RecognitionRate(2, 10),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'U' -> RecognitionRate(0, 10),
            'A' -> RecognitionRate(3, 10),
            'P' -> RecognitionRate(4, 10),
            'T' -> RecognitionRate(1, 10),
            'L' -> RecognitionRate(2, 10),
            'O' -> RecognitionRate(8, 10),
            'W' -> RecognitionRate(3, 10),
            'I' -> RecognitionRate(2, 10)
        )
        EasyMock.expectLastCall().andReturn(rates).anyTimes()

        mockPrefs.newCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        mockPrefs.missedCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        mockPrefs.similarCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        mockPrefs.setCharacterRecognitionRates(Map(
            ' ' -> RecognitionRate(0, 0),

            'E' -> RecognitionRate(0, 0),
            'X' -> RecognitionRate(0, 0),
            '8' -> RecognitionRate(0, 0),
            '4' -> RecognitionRate(0, 0),
            '.' -> RecognitionRate(0, 0),
            '9' -> RecognitionRate(0, 0),
            'N' -> RecognitionRate(0, 0),
            'T' -> RecognitionRate(1, 10),
            '=' -> RecognitionRate(0, 0),
            'Y' -> RecognitionRate(0, 0),
            'J' -> RecognitionRate(0, 0),
            'U' -> RecognitionRate(0, 10),
            'F' -> RecognitionRate(0, 0),
            'A' -> RecognitionRate(3, 10),
            '5' -> RecognitionRate(0, 0),
            'M' -> RecognitionRate(2, 10),
            'I' -> RecognitionRate(2, 10),
            ',' -> RecognitionRate(0, 0),
            'G' -> RecognitionRate(0, 0),
            '6' -> RecognitionRate(0, 0),
            '1' -> RecognitionRate(0, 0),
            'V' -> RecognitionRate(0, 0),
            'Q' -> RecognitionRate(0, 0),
            'L' -> RecognitionRate(2, 10),
            'B' -> RecognitionRate(0, 0),
            'P' -> RecognitionRate(4, 10),
            '0' -> RecognitionRate(0, 0),
            '?' -> RecognitionRate(0, 0),
            '2' -> RecognitionRate(0, 0),
            'C' -> RecognitionRate(0, 0),
            'H' -> RecognitionRate(0, 0),
            '+' -> RecognitionRate(0, 0),
            'W' -> RecognitionRate(3, 10),
            '7' -> RecognitionRate(0, 0),
            'K' -> RecognitionRate(1, 10),
            'R' -> RecognitionRate(10, 10),
            '3' -> RecognitionRate(0, 0),
            'O' -> RecognitionRate(8, 10),
            '/' -> RecognitionRate(0, 0),
            'D' -> RecognitionRate(0, 0),
            'Z' -> RecognitionRate(0, 0),
            'S' -> RecognitionRate(10, 10)))
        EasyMock.replay(mockPrefs)

        val recognitionRatePersister = new RecognitionRatePersister(mockPrefs)
        val tg = new TextGenerator(mockPrefs, recognitionRatePersister)
        tg.start(Koch)
        val words = new ArrayBuffer[MorseChar]()
        for (i <- 0 to 40) {
            val next = tg.next()
            words += next
            LOGGER.info("next char is " + next)
        }

        EasyMock.verify(mockPrefs)
        LOGGER.info("All words sent '" + words.toSeq.mkString + "'")
    }


    @Test
    def testWorstGenerator() {
        val mockPrefs = EasyMock.createMock(classOf[MorseTrainerPrefs])

        mockPrefs.getKochLevel
        EasyMock.expectLastCall().andReturn(12)

        mockPrefs.getCharacterRecognitionRates
        val rates: Map[MorseChar, RecognitionRate] = Map(
            'K' -> RecognitionRate(9, 10),
            'M' -> RecognitionRate(9, 10),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'U' -> RecognitionRate(0, 10),
            'A' -> RecognitionRate(1, 10),
            'P' -> RecognitionRate(9, 10),
            'T' -> RecognitionRate(9, 10),
            'L' -> RecognitionRate(9, 10),
            'O' -> RecognitionRate(8, 10),
            'W' -> RecognitionRate(9, 10),
            'I' -> RecognitionRate(2, 10)
        )
        EasyMock.expectLastCall().andReturn(rates).anyTimes()

        mockPrefs.setCharacterRecognitionRates(Map(
            ' ' -> RecognitionRate(0, 0),

            'E' -> RecognitionRate(0, 0),
            'X' -> RecognitionRate(0, 0),
            '8' -> RecognitionRate(0, 0),
            '4' -> RecognitionRate(0, 0),
            '.' -> RecognitionRate(0, 0),
            '9' -> RecognitionRate(0, 0),
            'N' -> RecognitionRate(0, 0),
            'T' -> RecognitionRate(9, 10),
            '=' -> RecognitionRate(0, 0),
            'Y' -> RecognitionRate(0, 0),
            'J' -> RecognitionRate(0, 0),
            'U' -> RecognitionRate(0, 10),
            'F' -> RecognitionRate(0, 0),
            'A' -> RecognitionRate(1, 10),
            '5' -> RecognitionRate(0, 0),
            'M' -> RecognitionRate(9, 10),
            'I' -> RecognitionRate(2, 10),
            ',' -> RecognitionRate(0, 0),
            'G' -> RecognitionRate(0, 0),
            '6' -> RecognitionRate(0, 0),
            '1' -> RecognitionRate(0, 0),
            'V' -> RecognitionRate(0, 0),
            'Q' -> RecognitionRate(0, 0),
            'L' -> RecognitionRate(9, 10),
            'B' -> RecognitionRate(0, 0),
            'P' -> RecognitionRate(9, 10),
            '0' -> RecognitionRate(0, 0),
            '?' -> RecognitionRate(0, 0),
            '2' -> RecognitionRate(0, 0),
            'C' -> RecognitionRate(0, 0),
            'H' -> RecognitionRate(0, 0),
            '+' -> RecognitionRate(0, 0),
            'W' -> RecognitionRate(9, 10),
            '7' -> RecognitionRate(0, 0),
            'K' -> RecognitionRate(9, 10),
            'R' -> RecognitionRate(10, 10),
            '3' -> RecognitionRate(0, 0),
            'O' -> RecognitionRate(8, 10),
            '/' -> RecognitionRate(0, 0),
            'D' -> RecognitionRate(0, 0),
            'Z' -> RecognitionRate(0, 0),
            'S' -> RecognitionRate(10, 10)))
        EasyMock.replay(mockPrefs)

        val recognitionRatePersister = new RecognitionRatePersister(mockPrefs)
        val tg = new TextGenerator(mockPrefs, recognitionRatePersister)
        tg.start(Worst)
        val words = new ArrayBuffer[MorseChar]()
        for (i <- 0 to 40) {
            val next = tg.next()
            words += next
            LOGGER.info("next char is " + next)
        }

        EasyMock.verify(mockPrefs)
        LOGGER.info("All words sent (should only contain UAOI) '" + words.toSeq.mkString + "'")
        // uaoi
    }

    @Test
    def testFreestyleGenerator() {
        val mockPrefs = EasyMock.createMock(classOf[MorseTrainerPrefs])

//        mockPrefs.getKochLevel
//        EasyMock.expectLastCall().andReturn(12)

        mockPrefs.getCharacterRecognitionRates
        val rates: Map[MorseChar, RecognitionRate] = Map(
            'K' -> RecognitionRate(4, 10),
            'M' -> RecognitionRate(2, 10),
            'R' -> RecognitionRate(10, 10)
        )
        EasyMock.expectLastCall().andReturn(rates).anyTimes()

        mockPrefs.setCharacterRecognitionRates(Map(
            ' ' -> RecognitionRate(0, 0),

            'E' -> RecognitionRate(0, 0),
            'X' -> RecognitionRate(0, 0),
            '8' -> RecognitionRate(0, 0),
            '4' -> RecognitionRate(0, 0),
            '.' -> RecognitionRate(0, 0),
            '9' -> RecognitionRate(0, 0),
            'N' -> RecognitionRate(0, 0),
            'T' -> RecognitionRate(0, 0),
            '=' -> RecognitionRate(0, 0),
            'Y' -> RecognitionRate(0, 0),
            'J' -> RecognitionRate(0, 0),
            'U' -> RecognitionRate(0, 0),
            'F' -> RecognitionRate(0, 0),
            'A' -> RecognitionRate(0, 0),
            '5' -> RecognitionRate(0, 0),
            'M' -> RecognitionRate(2, 10),
            'I' -> RecognitionRate(0, 0),
            ',' -> RecognitionRate(0, 0),
            'G' -> RecognitionRate(0, 0),
            '6' -> RecognitionRate(0, 0),
            '1' -> RecognitionRate(0, 0),
            'V' -> RecognitionRate(0, 0),
            'Q' -> RecognitionRate(0, 0),
            'L' -> RecognitionRate(0, 0),
            'B' -> RecognitionRate(0, 0),
            'P' -> RecognitionRate(0, 0),
            '0' -> RecognitionRate(0, 0),
            '?' -> RecognitionRate(0, 0),
            '2' -> RecognitionRate(0, 0),
            'C' -> RecognitionRate(0, 0),
            'H' -> RecognitionRate(0, 0),
            '+' -> RecognitionRate(0, 0),
            'W' -> RecognitionRate(0, 0),
            '7' -> RecognitionRate(0, 0),
            'K' -> RecognitionRate(4, 10),
            'R' -> RecognitionRate(10, 10),
            '3' -> RecognitionRate(0, 0),
            'O' -> RecognitionRate(0, 0),
            '/' -> RecognitionRate(0, 0),
            'D' -> RecognitionRate(0, 0),
            'Z' -> RecognitionRate(0, 0),
            'S' -> RecognitionRate(0, 0)))
        EasyMock.replay(mockPrefs)

        val recognitionRatePersister = new RecognitionRatePersister(mockPrefs)
        val tg = new TextGenerator(mockPrefs, recognitionRatePersister)
        tg.start(Freestyle, Set('K', 'M', 'R'))
        val words = new ArrayBuffer[MorseChar]()
        for (i <- 0 to 40) {
            val next = tg.next()
            words += next
            LOGGER.info("next char is " + next)
        }

        EasyMock.verify(mockPrefs)
        LOGGER.info("All words sent (should only contain KM) '" + words.toSeq.mkString + "'")
        // KM
    }
}
