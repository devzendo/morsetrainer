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

class TestProbabilityMap extends AssertionsForJUnit with MustMatchersForJUnit {
    val set = Set('a', 'b', 'c')
    def abHigh(ch: Char) = if (ch == 'a' || ch == 'b') 0.75 else 0.25

    val setPM = new SetFnProbabilityMap[Char](set, abHigh)
    val definitePM = new DefiniteProbabilityMap[Char](set)
    val definiteAPM = new DefiniteProbabilityMap[Char](Set('a'))
    val mapPM = new MapBasedProbabilityMap[Char](Map(
        'a' -> 0.75,
        'b' -> 0.75,
        'c' -> 0.25))

    @Test
    def setBased() {
        setPM.get('a') must equal(0.75)
        setPM.get('b') must equal(0.75)
        setPM.get('c') must equal(0.25)

        setPM.get('z') must equal(0.0)

        setPM.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.75,
            'c' -> 0.25))
    }

    @Test
    def definiteSet() {

        definitePM.get('a') must equal(1.0)
        definitePM.get('b') must equal(1.0)
        definitePM.get('c') must equal(1.0)

        definitePM.get('z') must equal(0.0)

        definitePM.getMap must equal(Map(
            'a' -> 1.0,
            'b' -> 1.0,
            'c' -> 1.0))
    }

    @Test
    def defMultSet() {
        val mult = definiteAPM * setPM
        mult.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.0,
            'c' -> 0.0
        ))
    }

    @Test
    def setMultDef() {
        val mult = setPM * definiteAPM
        mult.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.0,
            'c' -> 0.0
        ))
    }

    @Test
    def mapBased() {
        mapPM.get('a') must equal(0.75)
        mapPM.get('b') must equal(0.75)
        mapPM.get('c') must equal(0.25)

        mapPM.get('z') must equal(0.0)

        mapPM.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.75,
            'c' -> 0.25))
    }

    @Test
    def defMultMap() {
        val mult = definiteAPM * mapPM
        mult.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.0,
            'c' -> 0.0
        ))
    }

    @Test
    def mapMultDef() {
        val mult = mapPM * definiteAPM
        mult.getMap must equal(Map(
            'a' -> 0.75,
            'b' -> 0.0,
            'c' -> 0.0
        ))
    }

    @Test
    def setMultMap() {
        val mult = setPM * mapPM
        mult.getMap must equal(Map(
            'a' -> 0.75 * 0.75,
            'b' -> 0.75 * 0.75,
            'c' -> 0.25 * 0.25
        ))
    }

    @Test
    def mapMultSet() {
        val mult = mapPM * setPM
        mult.getMap must equal(Map(
            'a' -> 0.75 * 0.75,
            'b' -> 0.75 * 0.75,
            'c' -> 0.25 * 0.25
        ))
    }

    @Test
    def generateRangeArray() {
        // these probabilities sum to 1.0, but they don't have to. the range
        // will be scaled to the sum of all probabilities.
        val rangeMap = new MapBasedProbabilityMap[Char](
            Map('a' -> 0.25, 'b' -> 0.15, 'c' -> 0.35, 'd' -> 0.05, 'e' -> 0.20))

        val rangeArray = rangeMap.generateRangeArray()
        rangeArray.size must equal(100)
        rangeArray.count( _ == 'a' ) must equal(25)
        rangeArray.count( _ == 'b' ) must equal(15)
        rangeArray.count( _ == 'c' ) must equal(35)
        rangeArray.count( _ == 'd' ) must equal(5)
        rangeArray.count( _ == 'e' ) must equal(20)
    }

    @Test
    def probabilisticallyGetChars() {
        val rangeMap = new MapBasedProbabilityMap[Char](
            Map('a' -> 0.25, 'b' -> 0.15, 'c' -> 0.35, 'd' -> 0.05, 'e' -> 0.20))
        val probChars = (1 to 100).map { i: Int => { rangeMap.getProbabilistically._1 } }
        println("probabilisticallyGetChars")
        println("a (25) " + probChars.count( _ == 'a'))
        println("b (15) " + probChars.count( _ == 'b'))
        println("c (35) " + probChars.count( _ == 'c'))
        println("d (5)  " + probChars.count( _ == 'd'))
        println("e (20) " + probChars.count( _ == 'e'))

    }

    @Test
    def probabilisticallyGetDefiniteChars() {
        val probChars = (1 to 100).map { i: Int => { definitePM.getProbabilistically._1 } }
        println("probabilisticallyGetDefiniteChars")
        println("a (33) " + probChars.count( _ == 'a'))
        println("b (33) " + probChars.count( _ == 'b'))
        println("c (33) " + probChars.count( _ == 'c'))
        println("d (0)  " + probChars.count( _ == 'd'))
        println("e (0)  " + probChars.count( _ == 'e'))
    }
}