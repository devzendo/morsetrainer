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

import org.devzendo.morsetrainer.ProbabilityMap.Probability
import scala.reflect.ClassTag

object ProbabilityMap {
    type Probability = Double
}

abstract class ProbabilityMap[T: ClassTag] {
    def get(t: T): Probability
    def getMap: Map[T, Probability]

    def *(other: ProbabilityMap[T]): ProbabilityMap[T] = {
        val allKeys = getMap.keySet.union(other.getMap.keySet)
        val out = allKeys.map ( {t: T => (t, get(t) * other.get(t)) } ).toMap
        new MapBasedProbabilityMap(out)
    }

    /*
    a 25%
    b 15%
    c 35%
    d 5%
    e 20%
    assign each item a range calculated as
    start - sum of prob of all items before
    end - start + own probability
    a 0 - 25
    b 26 - 40
    c 41 - 75
    d 76 - 80
    e 81 - 100
    then pick random(0..100)
    return char in whose range that lies

    fill in the proportional number of each char in an array of 100 chars for rapid lookup
     */
    def generateRangeArray(): Array[T] = {
        val outArray = new Array[T](100)
        def fill(t: T, start: Int, end: Int) {
            for (i <- start until end) {
                outArray(i) = t
            }
        }

        val sumProb = (0.0 /: getMap.values) (_ + _)
        var currProb = 0.0
        for (k <- getMap.keysIterator) {
            val thisProb = get(k)
            val scaledProb = thisProb / sumProb
            fill(k, (currProb * 100).toInt, ((currProb + scaledProb) * 100).toInt)
            currProb += scaledProb
        }

        outArray
    }

    lazy val rangeArray = generateRangeArray()

    def getProbabilistically: Tuple2[T, Probability] = {
        val randPercentage = (Math.random() * 99.0).toInt
        val probabilisticValue = rangeArray(randPercentage)
        (probabilisticValue, get(probabilisticValue))
    }

    override def toString(): String = {
        getMap.toString
    }
}

class SetFnProbabilityMap[T: ClassTag](tSet: Set[T], probFn: T => Probability) extends ProbabilityMap[T] {
    val probMap = tSet.map( { t: T => (t, probFn(t)) } ).toMap

    def get(t: T) = probMap.getOrElse(t, 0.0)
    def getMap = probMap
}

class MapBasedProbabilityMap[T: ClassTag](inMap: Map[T, Probability]) extends ProbabilityMap[T] {

    def get(t: T) = inMap.getOrElse(t, 0.0)
    def getMap = inMap
}

class DefiniteProbabilityMap[T: ClassTag](inSet: Set[T]) extends ProbabilityMap[T] {
    val probMap = inSet.map( { t: T => (t, 1.0) } ).toMap

    def get(t: T) = probMap.getOrElse(t, 0.0)
    def getMap = probMap
}
