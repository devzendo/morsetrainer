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
import org.devzendo.morsetrainer.TextSpacingIterator.OptionMorseCharClipRequestsWithTotalDuration

object TestTextSpacingIterator {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestTextSpacingIterator])

    val textToMorse = new TextToMorseClipRequests()
    val clipGeneratorHolder = new ClipGeneratorHolder(18, true, 12, 600)
    val reader = new TextAsMorseReader(textToMorse, clipGeneratorHolder)

    implicit def str2iter(str: String): Iterator[MorseChar] = str.iterator
    def getIterator(str: String) = new TextSpacingIterator(str, reader)
}

class TestTextSpacingIterator extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestTextSpacingIterator._

    def finished(tsi: TextSpacingIterator) {
        tsi.hasNext must equal(false)
        val thrown = evaluating {
            tsi.next()
        } must produce[IllegalStateException]
        thrown.getMessage must equal("Cannot get next from a spent Iterator")
    }

    def next(tsi: TextSpacingIterator): OptionMorseCharClipRequestsWithTotalDuration = {
        tsi.hasNext must equal(true)
        tsi.next()
    }

    @Test
    def emptyIterator() {
        LOGGER.info("emptyIterator")
        val tsi = getIterator("")

        finished(tsi)
    }

    @Test
    def singleCharacter() {
        LOGGER.info("singleCharacter")
        val tsi = getIterator("A")

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))

        finished(tsi)
    }

    @Test
    def twoCharacters() {
        LOGGER.info("twoCharacters")
        val tsi = getIterator("AB")

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))

        next(tsi) must be((None, List(CharSp), 297))

        next(tsi) must be((Some('B'), List(Dah, ElementSp, Dit, ElementSp, Dit, ElementSp, Dit), 594)) // ?

        finished(tsi)
    }

    @Test
    def charSpace() {
        LOGGER.info("charSpace")
        val tsi = getIterator("A ")

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))

        next(tsi) must be((Some(' '), List(WordSp), 693))

        finished(tsi)
    }

    @Test
    def spaceChar() {
        LOGGER.info("spaceChar")
        val tsi = getIterator(" A")

        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))

        finished(tsi)
    }

    @Test
    def twoSpaces() {
        LOGGER.info("twoSpaces")
        val tsi = getIterator("  ")

        next(tsi) must be((Some(' '), List(WordSp), 693))
        next(tsi) must be((Some(' '), List(WordSp), 693))

        finished(tsi)
    }

    @Test
    def twoSpacesBounded() {
        LOGGER.info("twoSpacesBounded")
        val tsi = getIterator("E  E")

        next(tsi) must be((Some('E'), List(Dit), 66))

        next(tsi) must be((Some(' '), List(WordSp), 693))
        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('E'), List(Dit), 66))

        finished(tsi)
    }

    @Test
    def twoCharactersSeparatedBySpace() {
        LOGGER.info("twoCharactersSeparatedBySpace")
        val tsi = getIterator("A B")

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))

        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('B'), List(Dah, ElementSp, Dit, ElementSp, Dit, ElementSp, Dit), 594))

        finished(tsi)
    }

    @Test
    def mixed() {
        LOGGER.info("mixed")
        val tsi = getIterator(" ABC CD E ")

        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('A'), List(Dit, ElementSp, Dah), 330))
        next(tsi) must be((None, List(CharSp), 297))
        next(tsi) must be((Some('B'), List(Dah, ElementSp, Dit, ElementSp, Dit, ElementSp, Dit), 594))
        next(tsi) must be((None, List(CharSp), 297))
        next(tsi) must be((Some('C'), List(Dah, ElementSp, Dit, ElementSp, Dah, ElementSp, Dit), 726))

        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('C'), List(Dah, ElementSp, Dit, ElementSp, Dah, ElementSp, Dit), 726))
        next(tsi) must be((None, List(CharSp), 297))
        next(tsi) must be((Some('D'), List(Dah, ElementSp, Dit, ElementSp, Dit), 462))

        next(tsi) must be((Some(' '), List(WordSp), 693))

        next(tsi) must be((Some('E'), List(Dit), 66))

        next(tsi) must be((Some(' '), List(WordSp), 693))

        finished(tsi)
    }
}
