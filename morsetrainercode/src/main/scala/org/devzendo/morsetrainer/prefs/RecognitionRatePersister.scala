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

package org.devzendo.morsetrainer.prefs

import org.devzendo.morsetrainer.Morse.MorseChar
import org.devzendo.morsetrainer._
import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.Key

object RecognitionRatePersister {
    private val LOGGER = LoggerFactory.getLogger(classOf[RecognitionRatePersister])
}

class RecognitionRatePersister(prefs: MorseTrainerPrefs) {
    import RecognitionRatePersister._

    var typedKeys = ArrayBuffer[MorseChar]()
    val sentChars = ArrayBuffer[MorseChar]()
    var startMap: Map[MorseChar, RecognitionRate] = initialise()
    val sessionMap = scala.collection.mutable.Map[MorseChar, RecognitionRate]()

    def reset() {
        LOGGER.debug("Resetting")
        startMap = getStartMap
        sessionMap.clear()
        typedKeys.clear()
        sentChars.clear()
    }

    def persist() {
        LOGGER.debug("Persisting")
        prefs.setCharacterRecognitionRates(getRecognitionRates(Morse.chars.toSet))
    }

    private def recompute() {
        val start = System.currentTimeMillis()
        LOGGER.debug("Recomputing")

        val typedString = new String(typedKeys.toArray)
        LOGGER.debug("Typed chars '" + typedString + "'")
        val sentString = new String(sentChars.toArray)
        LOGGER.debug("Sent chars '" + sentString + "'")
        val uniqueSentChars = sentChars.toSet
        LOGGER.debug("Unique sent chars: " + uniqueSentChars)

        sessionMap.clear()

        val matcher = new EditMatcher(sentString, typedString)
        val edits = matcher.edits()
        LOGGER.debug("Edits: " + edits)
        val matchingEdits = edits.filter {
            case Match(_) => true
            case _ => false
        }
        LOGGER.debug("Matching edits: " + matchingEdits)

        uniqueSentChars.foreach( (ch: MorseChar) => {
            LOGGER.debug("Getting start rate for '" + ch + "'")

            val startRate = startMap.get(ch)
            startRate match {
                case Some(rate) => {
                    LOGGER.debug("Start rate for " + ch + " is " + rate)
                    val numMatched = matchingEdits.count {
                        case Match(mch) => mch == ch
                        case _ => false
                    }
                    LOGGER.debug("Num matched: " + numMatched)
                    val numSent = sentChars.count(_ == ch)
                    LOGGER.debug("Num sent: " + numSent)
                    val update = ch -> RecognitionRate(rate.correct + numMatched, rate.sentInTotal + numSent)
                    LOGGER.debug("Updating Session Map with: " + update)
                    sessionMap += update
                }
                case None => {
                    val msg = "Why am I being asked for the rate of '" + ch + "' that I don't store?"
                    LOGGER.warn(msg)
                    throw new IllegalStateException(msg)
                }
            }
        } )
        LOGGER.debug("Session Map: " + sessionMap)
        val duration = System.currentTimeMillis() - start
        LOGGER.info("Recomputation took " + duration + " ms")
    }

    def keyReceived(key: KeyboardEvent) {
        LOGGER.info("Received key: " + key)
        key match {
            case Backspace => {
                if (!typedKeys.isEmpty) {
                    typedKeys = typedKeys.init
                    recompute()
                }
            }
            case Key(ch: Char) =>
                typedKeys += ch
                recompute()
        }
    }

    def charPlayed(ch: MorseChar) {
        LOGGER.info("Played: '" + ch + "'")
        sentChars += ch
        recompute()
    }

    def getStartMap: Map[Morse.MorseChar, RecognitionRate] = {
        val initialRates = prefs.getCharacterRecognitionRates
        assert(initialRates != null)
        // TODO should Prefs initialise all chars+space to 0/0 or this?
        def charToCharPlusRecognitionRate(ch: MorseChar): (MorseChar, RecognitionRate) = {
            val rr = initialRates.getOrElse(ch, RecognitionRate(0, 0))
            (ch, rr)
        }
        (Morse.chars + ' ').map(charToCharPlusRecognitionRate).toMap
    }

    def initialise(): Map[MorseChar, RecognitionRate] = {
        LOGGER.debug("Initialising")
        val startMap = getStartMap
        LOGGER.debug("initialised start map " + startMap)
        prefs.setCharacterRecognitionRates(startMap)
        startMap
    }

    def getRecognitionRates(forChars: Set[MorseChar]): Map[MorseChar, RecognitionRate] = {
        LOGGER.debug("Getting rates for '" + forChars + "'")
        LOGGER.debug("session map " + sessionMap)
        LOGGER.debug("start map " + startMap)

        def getFromSessionOrStart(ch: MorseChar) = {
            sessionMap.getOrElse(ch, startMap.getOrElse(ch, RecognitionRate(0, 0)))
        }
        forChars.map( { ch: MorseChar => (ch, getFromSessionOrStart(ch)) } ).toMap
    }
}
