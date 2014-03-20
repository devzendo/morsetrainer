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
import org.slf4j.LoggerFactory
import org.junit.Test
import org.devzendo.morsetrainer.Morse.MorseChar

object TestSpacingIterator {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestSpacingIterator])

    val textToMorse = new TextToMorseClipRequests()
    val clipGeneratorHolder = new ClipGeneratorHolder(18, true, 12, 600)
    val reader = new TextAsMorseReader(textToMorse, clipGeneratorHolder)

    implicit def str2iter(str: String): Iterator[MorseChar] = str.iterator
    def getIterator(str: String) = new TextSpacingIterator(str, reader)
}

class TestSpacingIterator extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestSpacingIterator._


    @Test
    def emptyIterator() {
        LOGGER.info("emptyIterator")
        val tsi = getIterator("")

        tsi.hasNext must equal(false)
        val thrown = evaluating {
            tsi.next()
        } must produce[IllegalStateException]
        thrown.getMessage must equal("Cannot get next from a spent Iterator")
    }
}
