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

import org.slf4j.LoggerFactory

sealed abstract class Edit(val ch: Char)
case class Match(override val ch: Char) extends Edit(ch)
case class Deletion(override val ch: Char) extends Edit(ch)
case class Mutation(override val ch: Char) extends Edit(ch)

/**
 * A Levenshtein distance calculator, that also returns the list of edits.
 * With gratitude to Matt Malone, whose
 * http://oldfashionedsoftware.com/2009/11/19/string-distance-and-refactoring-in-scala/
 * was a great help in improving my understanding of the algorithm.
 */
object EditMatcher {
    private val LOGGER = LoggerFactory.getLogger(EditMatcher.getClass)
}

class EditMatcher(s1: String, s2: String) {
    import EditMatcher._

    //            LOGGER.info("H s1: '" + s1 + "' length " + s1.length)
    //            LOGGER.info("V s2: '" + s2 + "' length " + s2.length)

    val dist = Array.ofDim[DistEdit](s1.length + 1, s2.length + 1)
    for (x <- 0 to s1.length; y <- 0 to s2.length) {
        dist(x)(y) = DistEdit(0, List())
    }

    for (x <- 1 to s1.length) dist(x)(0) = DistEdit(x, List(Deletion(s1(x-1))))
    for (y <- 1 to s2.length) dist(0)(y) = DistEdit(y, List(Deletion(s2(y-1))))

    /*
            t a p e
          0 1 2 3 4
        h 1 1 2 3 4
        a 2 2 1 2 3
        t 3 2 2 2 3
     */

    for (x <- 1 to s1.length; y <- 1 to s2.length) {
        val delFromS1 = {
            val de = dist(x-1)(y  )
            DistEdit(de.dist + 1, de.edits ::: List(Deletion(s1(x-1))))
        }
        val delFromS2 = {
            val de = dist(x)(y - 1)
            DistEdit(de.dist + 1, de.edits ::: List(Deletion(s2(y-1))))
        }
        val delFromS1AndS2 = {
            val de = dist(x-1)(y-1)
            if (s1(x-1) == s2(y-1)) {
                DistEdit(de.dist + 0, de.edits ::: List(Match(s1(x-1))))
            } else {
                DistEdit(de.dist + 1, de.edits ::: List(Mutation(s1(x-1))))
            }
        }
        dist(x)(y) = minimum (
            delFromS1,
            delFromS2,
            delFromS1AndS2
        )
    }

    //            LOGGER.info("final matrix:")
    //            display()
    private val finalCell = dist(s1.length)(s2.length)
    //            LOGGER.info("Levenshtein distance of '" + s1 + "' to '" + s2 + "' is " + out.dist)
    //            LOGGER.info("Edits are: " + out.edits)

    case class DistEdit(dist:Int, edits: List[Edit])

    def display() {
        LOGGER.info("        " + s1.map{"%3s ".format(_)}.mkString(" "))
        for (y <- 0 to s2.length) {
            val line = for (x <- 0 to s1.length) yield dist(x)(y).dist
            LOGGER.info("%3s".format(if (y==0) ' ' else s2(y-1)) + line.map{"%3s ".format(_)}.mkString(" "))
        }
    }

    private def minimum(i1: DistEdit, i2: DistEdit, i3: DistEdit) = {
        if (i1.dist < i2.dist) {
            if (i1.dist < i3.dist) {
                i1
            } else {
                i3
            }
        } else {
            // i1 >= i2
            if (i2.dist < i3.dist) {
                i2
            } else {
                i3
            }
        }
    }

    def edits(): List[Edit] = {
        finalCell.edits
    }

    def distance(): Int = {
        finalCell.dist
    }
}
