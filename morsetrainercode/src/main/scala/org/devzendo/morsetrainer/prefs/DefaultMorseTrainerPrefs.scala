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

import org.devzendo.commoncode.file.INIFile

import org.slf4j.LoggerFactory
import org.devzendo.morsetrainer.Morse
import org.devzendo.morsetrainer.Morse.MorseChar

/**
 * The .ini-file based implementation of MorseTrainerPrefs.
 *
 * @author matt
 *
 */
object DefaultMorseTrainerPrefs {
    private val LOGGER = LoggerFactory.getLogger(classOf[DefaultMorseTrainerPrefs])

    // The section names and preference items
    private val SECTION_UI = "ui"
    private val UI_GEOMETRY = "geometry"

    private val SECTION_MORSE = "morse"
    private val WPM = "wpm"
    private val USING_FARNSWORTH = "using_farnsworth"
    private val FARNSWORTH_WPM = "farnsworth_wpm"
    private val PULSE_FREQ = "pulse_freq"
    private val KOCH_LEVEL = "koch_level"

    private val SECTION_SESSION = "session"
    private val NEW_FREQUENTLY = "new_frequently"
    private val MISSED_FREQUENTLY = "missed_frequently"
    private val SIMILAR_FREQUENTLY = "similar_frequently"
    private val RANDOM_LENGTH_WORDS = "random_length_words"
    private val NON_RANDOM_WORD_LENGTH = "non_random_word_length"
    private val SESSION_LENGTH = "session_length"

    private val SECTION_RECOGNITION_RATES = "recognition"

    private val RATE_SPLIT_REGEX = """^(\d+)/(\d+)$""".r
}

/**
 * @param prefsFilePath the path at which the .ini file should be stored.
 *
 */
class DefaultMorseTrainerPrefs(prefsFilePath: String) extends MorseTrainerPrefs {
    import DefaultMorseTrainerPrefs._

    LOGGER.debug("Morse trainer preferences are stored at " + prefsFilePath)
    val iniFile = new INIFile(prefsFilePath)

    def getWindowGeometry(windowName: String): String = {
        iniFile.getValue(SECTION_UI, formWindowGeometryKey(windowName), "")
    }

    def setWindowGeometry(windowName: String, geometry: String) {
        iniFile.setValue(SECTION_UI, formWindowGeometryKey(windowName), geometry)
    }

    private def formWindowGeometryKey(windowName: String): String = {
        UI_GEOMETRY + "_" + windowName
    }


    /**
     * Words per minute, for dits, dahs and element spacing.
     * @return the wpm rate
     */
    def getWordsPerMinute: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_MORSE, WPM, "" + MorseTrainerPrefs.DefaultWordsPerMinute))
    }

    /**
     * Store the words per minute.
     * @return the wpm rate
     */
    def setWordsPerMinute(wpm: Int) {
        iniFile.setValue(SECTION_MORSE, WPM, "" + wpm)
    }

    def usingFarnsworth: Boolean = iniFile.getBooleanValue(SECTION_MORSE, USING_FARNSWORTH)

    def setUsingFarnsworth(using: Boolean) {
        iniFile.setBooleanValue(SECTION_MORSE, USING_FARNSWORTH, using)
    }

    /**
     * Farnsworth words per minute, for character and word spacing.
     * @return the Farnsworth wpm rate
     */
    def getFarnsworthWordsPerMinute: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_MORSE, FARNSWORTH_WPM, "" + MorseTrainerPrefs.DefaultFarnsworthWordsPerMinute))
    }

    /**
     * Store the Farnsworth words per minute.
     * @return the Farnsworth wpm rate
     */
    def setFarnsworthWordsPerMinute(fwpm: Int) {
        iniFile.setValue(SECTION_MORSE, FARNSWORTH_WPM, "" + fwpm)
    }

    /**
     * Pulse frequency in Hz.
     * @return the frequency
     */
    def getPulseFrequencyHz: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_MORSE, PULSE_FREQ, "" + MorseTrainerPrefs.DefaultPulseFrequency))
    }

    /**
     * Store the pulse frequency.
     * @return the frequency in Hz
     */
    def setPulseFrequency(hz: Int) {
        iniFile.setValue(SECTION_MORSE, PULSE_FREQ, "" + hz)
    }

    /**
     * Koch level.
     * @return the current level
     */
    def getKochLevel: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_MORSE, KOCH_LEVEL, "" + MorseTrainerPrefs.DefaultKochLevel))
    }

    /**
     * Store the Koch level.
     * @return the level
     */
    def setKochLevel(lvl: Int) {
        iniFile.setValue(SECTION_MORSE, KOCH_LEVEL, "" + lvl)
    }

    def newCharsMoreFrequently: Boolean = iniFile.getBooleanValue(SECTION_SESSION, NEW_FREQUENTLY)

    def setNewCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(SECTION_SESSION, NEW_FREQUENTLY, more)
    }

    def missedCharsMoreFrequently: Boolean = iniFile.getBooleanValue(SECTION_SESSION, MISSED_FREQUENTLY)

    def setMissedCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(SECTION_SESSION, MISSED_FREQUENTLY, more)
    }

    def similarCharsMoreFrequently: Boolean = iniFile.getBooleanValue(SECTION_SESSION, SIMILAR_FREQUENTLY)

    def setSimilarCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(SECTION_SESSION, SIMILAR_FREQUENTLY, more)
    }

    def areWordsRandomLength: Boolean = iniFile.getBooleanValue(SECTION_SESSION, RANDOM_LENGTH_WORDS)

    def setWordsRandomLength(random: Boolean) {
        iniFile.setBooleanValue(SECTION_SESSION, RANDOM_LENGTH_WORDS, random)
    }

    def getNonRandomWordLength: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_SESSION, NON_RANDOM_WORD_LENGTH, "" + MorseTrainerPrefs.DefaultNonRandomWordLength))
    }

    def setNonRandomWordLength(len: Int) {
        iniFile.setValue(SECTION_SESSION, NON_RANDOM_WORD_LENGTH, "" + len)
    }

    def getSessionLength: Int = {
        Integer.parseInt(iniFile.getValue(SECTION_SESSION, SESSION_LENGTH, "" + MorseTrainerPrefs.DefaultSessionLength))
    }

    def setSessionLength(len: Int) {
        iniFile.setValue(SECTION_SESSION, SESSION_LENGTH, "" + len)
    }

    private def getCharacterRecognitionRate(morseChar: MorseChar): RecognitionRate = {
        val rateStr = iniFile.getValue(SECTION_RECOGNITION_RATES, "rate_" + morseChar.toInt, "0/0")
        val RATE_SPLIT_REGEX(correct, numberSent) = rateStr
        RecognitionRate(Integer.parseInt(correct), Integer.parseInt(numberSent))
    }

    def getCharacterRecognitionRates: Map[MorseChar, RecognitionRate] = {
        Morse.chars.map( { ch: MorseChar => (ch, getCharacterRecognitionRate(ch)) } ).toMap
    }

    def setCharacterRecognitionRates(map: Map[Morse.MorseChar, RecognitionRate]) {
        iniFile.suspendWrite()
        Morse.chars.foreach {
            ch: MorseChar => {
                val rate = map.getOrElse(ch, RecognitionRate(0, 0)).toString
                iniFile.setValue(SECTION_RECOGNITION_RATES, "rate_" + ch.toInt, rate)
            }
        }
        iniFile.resumeWrite()
    }
}
