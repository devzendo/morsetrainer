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

sealed abstract class ClipRequest
case object Dit extends ClipRequest
case object Dah extends ClipRequest
case object ElementSp extends ClipRequest
case object CharSp extends ClipRequest
case object WordSp extends ClipRequest

class TextToMorse {
    import Morse._

    private def charToClipRequest(ch: Char): ClipRequest = if (ch == '-') Dah else Dit

    // There's got to be a standard function for this...
    private def intersperse[X](xs: List[X], xsep: X): List[X] = {
        var ooo = scala.collection.mutable.ListBuffer[X]()
        for (i <- 0 until xs.size - 1) {
            ooo += xs(i)
            ooo += xsep
        }
        ooo += xs.last
        ooo.toList
//      tried this, but failed
//        def addSepAfterEach(x: X): (X, X) = (x, xsep)
//        val added = xs map { addSepAfterEach }
//        val interspersed = added.init
//        val out = interspersed.flatten
//        out
    }

    // Individual letter in morse to clip requests, interspersed with element spacing
    type LetterClipRequests = List[ClipRequest]
    private def morseStringToElementSpacedListOfClipRequest(optMS: Option[MorseString]): LetterClipRequests = {
        // there's got to be a less clunky, functional way of doing this, but
        // i'm buggered if i can find it.
        // attempt one:
//        val clipRequests = for {
//            ms <- optMS
//            ditdah <- {
//                ms.map(charToClipRequest)
//            }
//        } yield ditdah
        // attempt two:
//        val clipRequests = optMS.flatMap( _.map(charToClipRequest) )

        optMS match {
            case None => List[ClipRequest]()
            case Some(ms) => {
                val clipRequests = ms.map(charToClipRequest)
                val clipRequestsList = clipRequests.toList
                intersperse[ClipRequest](clipRequestsList, ElementSp)
            }
        }
    }

    /**
     * Translate characters in the known set to { - . (space) }
     * @param char single upper/lower/digit = / . , ? +
     * @return ClipRequests - morse equivalents containing - . interspersed with element spacing
     */
    private def charToElementSpacedListOfClipRequest(char: Char): List[ClipRequest] = {
        morseStringToElementSpacedListOfClipRequest(charMap.get(char))
    }

    // Words to words-in-clip-requests with each letter separated with character spacing
    type WordClipRequests = List[LetterClipRequests]
    private def wordToListListClipRequest(word: String): WordClipRequests = {
        val wordLLCR: WordClipRequests = (word map charToElementSpacedListOfClipRequest).toList
        intersperse[LetterClipRequests](wordLLCR, List(CharSp))
    }

    /**
     * Translate a string of space-separated words into individual WordStrings
     * @param sentence space-separated words
     * @return a list of ClipRequests
     */
    def translateString(sentence: String): List[ClipRequest] = {
        val words: List[String] = sentence.toLowerCase.split("\\s").toList.filterNot( _.length == 0)
        if (words.isEmpty) {
            List[ClipRequest]()
        } else {
            val wordClipRequests: List[WordClipRequests] = (words map wordToListListClipRequest).toList
            val wordsClipRequestsInterspersed = intersperse[WordClipRequests](wordClipRequests, List(List(WordSp)))

            wordsClipRequestsInterspersed.flatten.flatten
        }
    }
}
