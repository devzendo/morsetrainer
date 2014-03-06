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
import org.devzendo.morsetrainer.Morse.MorseChar

class TestKochLevels extends AssertionsForJUnit with MustMatchersForJUnit {

    val fullSet: Set[MorseChar] = Set('K', 'M', 'R', 'S', 'U', 'A',
        'P', 'T', 'L', 'O', 'W', 'I', '.', 'N', 'J', 'E', 'F', '0', 'Y', ',',
        'V', 'G', '5', '/', 'Q', '9', 'Z', 'H', '3', '8', 'B', '?', '4', '2',
        '7', 'C', '1', 'D', '6', 'X', '=', '+')

    @Test
    def levelMustBe2OrMore_1() {
        failLevel(1)
    }

    @Test
    def levelMustBe2OrMore_0() {
        failLevel(0)
    }

    @Test
    def levelMustBe2OrMore_negative() {
        failLevel(-1)
    }

    private def failLevel(level: Int) {
        val thrown = evaluating {
            KochLevels.morseCharsForLevel(level)
        } must produce[IllegalArgumentException]
        thrown.getMessage must equal("Koch level must be >= 2")
    }

    @Test
    def firstLevel() {
        KochLevels.morseCharsForLevel(2) must equal(Set('K', 'M'))
    }

    @Test
    def fifthLevel() {
        KochLevels.morseCharsForLevel(5) must equal(Set('K', 'M', 'R', 'S', 'U'))
    }

    @Test
    def penultimateLevel() {
        val penultimateSet = fullSet - '+'
        KochLevels.morseCharsForLevel(41) must equal(penultimateSet)
    }

    @Test
    def lastLevel() {
        KochLevels.morseCharsForLevel(42) must equal(fullSet)
    }

    @Test
    def afterLastLevel() {
        KochLevels.morseCharsForLevel(43) must equal(fullSet)
    }
}
