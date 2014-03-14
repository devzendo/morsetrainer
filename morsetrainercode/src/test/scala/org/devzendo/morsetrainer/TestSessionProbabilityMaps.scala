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
import org.junit.Test
import org.devzendo.morsetrainer.prefs.RecognitionRate
import org.slf4j.LoggerFactory

object TestSessionProbabilityMaps {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestSessionProbabilityMaps])
}
class TestSessionProbabilityMaps extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestSessionProbabilityMaps._

    @Test
    def genNewCharsProbMapLevel2() {
        val map = SessionProbabilityMaps.genNewCharsProbMap(2)
        LOGGER.info("map is " + map.getMap)
    }

    @Test
    def genNewCharsProbMapLevel8() {
        val map = SessionProbabilityMaps.genNewCharsProbMap(8)
        LOGGER.info("map is " + map.getMap)
    }

    @Test
    def genMissedCharsProbMap() {
        val rfss = Map(
            'a' -> RecognitionRate(0, 6),
            'b' -> RecognitionRate(3, 6),
            'c' -> RecognitionRate(6, 6)
        )
        val map = SessionProbabilityMaps.genMissedCharsProbMap(rfss)
        LOGGER.info("genMissedCharsProbMap map is " + map.getMap)
    }

    @Test
    def genSimilarCharsProbMap() {
        val rfss = Map(
            'K' -> RecognitionRate(1, 10),  // under 90%
            'M' -> RecognitionRate(2, 10),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'U' -> RecognitionRate(10, 10)
        )
        val map = SessionProbabilityMaps.genSimilarCharsProbMap(Set('K', 'M', 'R', 'S', 'U'), rfss)
        LOGGER.info("genSimilarCharsProbMap map is " + map.getMap)
        map.get('K') must be(1.0) // has been increased since it's similar to R
    }

}
