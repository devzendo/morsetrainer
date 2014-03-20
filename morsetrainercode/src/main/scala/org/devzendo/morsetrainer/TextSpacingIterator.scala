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

object TextSpacingIterator {
    type OptionMorseCharClipRequestsWithTotalDuration = (Option[MorseChar], List[ClipRequest], Long)
}

class TextSpacingIterator(textGenerator: Iterator[MorseChar], textAsMorseReader: TextAsMorseReader) extends Iterator[OptionMorseCharClipRequestsWithTotalDuration] {
    // TODO introduce state machine....
    def hasNext: Boolean = textGenerator.hasNext

    def next(): OptionMorseCharClipRequestsWithTotalDuration = {
        if (!hasNext) {
            throw new IllegalStateException("Cannot get next from a spent Iterator")
        }

        val morseChar = textGenerator.next()
        val (clipRequests, duration) = textAsMorseReader.textToClipRequestsWithTotalDuration(morseChar.toString)
        (Some(morseChar), clipRequests, duration)
    }
}
