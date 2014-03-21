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

import org.devzendo.morsetrainer.Morse.MorseChar
import org.devzendo.morsetrainer.TextSpacingIterator.OptionMorseCharClipRequestsWithTotalDuration
import org.slf4j.LoggerFactory

object TextSpacingIterator {
    type OptionMorseCharClipRequestsWithTotalDuration = (Option[MorseChar], List[ClipRequest], Long)
    private val LOGGER = LoggerFactory.getLogger(classOf[TextSpacingIterator])
}

/*
 * A     -   A
 * A_    -   A wordsp
 * AB    -   A charsp B 
 * A_B   -   A wordsp B
 */
class TextSpacingIterator(textGenerator: Iterator[MorseChar], textAsMorseReader: TextAsMorseReader) extends Iterator[OptionMorseCharClipRequestsWithTotalDuration] {
    import TextSpacingIterator._

    var lookAhead: Option[MorseChar] = None
    var spaceRequired = false
    
    def hasNext: Boolean = {
        if (lookAhead.isDefined) true else textGenerator.hasNext
    }

    def fetchLookAhead() {
        if (textGenerator.hasNext) {
            lookAhead = Some(textGenerator.next())
        } else {
            lookAhead = None
        }
    }

    def gen(morseChar: MorseChar): OptionMorseCharClipRequestsWithTotalDuration = {
        val (clipRequests, duration) = textAsMorseReader.textToClipRequestsWithTotalDuration(morseChar.toString)

        (Some(morseChar), clipRequests, duration)
    }

    def genCharSpace(): OptionMorseCharClipRequestsWithTotalDuration = {
        val (clipRequests, duration) = textAsMorseReader.charSpaceClipRequestsWithTotalDuration

        (None, clipRequests, duration)
    }

    def genWordSpace(): OptionMorseCharClipRequestsWithTotalDuration = {
        gen(' ')
    }

    def next(): OptionMorseCharClipRequestsWithTotalDuration = {
        if (!hasNext) {
            throw new IllegalStateException("Cannot get next from a spent Iterator")
        }
        LOGGER.debug("before, lookAhead is " + lookAhead + " spaceRequired is " + spaceRequired)

        val out = lookAhead match {
            case Some(' ') => {
                spaceRequired = false
                val ret = genWordSpace()
                fetchLookAhead()
                ret
            }
            case Some(morseChar: MorseChar) =>
                if (spaceRequired) {
                    val ret = genCharSpace()
                    spaceRequired = false
                    ret
                } else {
                    val ret = gen(morseChar)
                    spaceRequired = morseChar != ' '
                    fetchLookAhead()
                    ret
                }
            case None => {
                val morseChar = textGenerator.next()
                LOGGER.debug("the next char from the text generator was " + morseChar)
                val ret = gen(morseChar)
                fetchLookAhead()
                spaceRequired = morseChar != ' '
                ret
            }
        }
        LOGGER.debug("after, lookAhead is " + lookAhead + " spaceRequired is " + spaceRequired)
        out
    }
}
