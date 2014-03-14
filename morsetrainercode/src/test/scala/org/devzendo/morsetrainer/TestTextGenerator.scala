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

object TestTextGenerator {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestTextGenerator])
}

class TestTextGenerator extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestTextGenerator._

    @Test
    def testKochGenerator() {
        val mockPrefs = EasyMock.createMock(classOf[MorseTrainerPrefs])

        mockPrefs.getKochLevel
        EasyMock.expectLastCall().andReturn(4)

        mockPrefs.getCharacterRecognitionRates
        val rates: Map[MorseChar, RecognitionRate] = Map(
            'K' -> RecognitionRate(1, 10),
            'M' -> RecognitionRate(2, 10),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'U' -> RecognitionRate(0, 10)
        )
        EasyMock.expectLastCall().andReturn(rates).anyTimes()

        mockPrefs.newCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        mockPrefs.missedCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        mockPrefs.similarCharsMoreFrequently
        EasyMock.expectLastCall().andReturn(true).anyTimes()

        EasyMock.replay(mockPrefs)

        val recognitionRatePersister = new RecognitionRatePersister(mockPrefs)
        val kg = new KochIterator(mockPrefs, recognitionRatePersister)
//        for (i <- 0 to 40) {
            LOGGER.info("next char is " + kg.next())
//        }

        EasyMock.verify(mockPrefs)
    }
    
}
