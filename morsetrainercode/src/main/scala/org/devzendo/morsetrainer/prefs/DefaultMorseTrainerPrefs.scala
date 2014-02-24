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

}

/**
 * @param prefsFilePath the path at which the .ini file should be stored.
 *
 */
class DefaultMorseTrainerPrefs(prefsFilePath: String) extends MorseTrainerPrefs {

    DefaultMorseTrainerPrefs.LOGGER.debug("Morse trainer preferences are stored at " + prefsFilePath)
    val iniFile = new INIFile(prefsFilePath)

    def getWindowGeometry(windowName: String): String = {
        iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_UI, formWindowGeometryKey(windowName), "")
    }

    def setWindowGeometry(windowName: String, geometry: String) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_UI, formWindowGeometryKey(windowName), geometry)
    }

    private def formWindowGeometryKey(windowName: String): String = {
        DefaultMorseTrainerPrefs.UI_GEOMETRY + "_" + windowName
    }


    /**
     * Words per minute, for dits, dahs and element spacing.
     * @return the wpm rate
     */
    def getWordsPerMinute: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.WPM, "" + MorseTrainerPrefs.DefaultWordsPerMinute))
    }

    /**
     * Store the words per minute.
     * @return the wpm rate
     */
    def setWordsPerMinute(wpm: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.WPM, "" + wpm)
    }

    def usingFarnsworth: Boolean = iniFile.getBooleanValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.USING_FARNSWORTH)

    def setUsingFarnsworth(using: Boolean) {
        iniFile.setBooleanValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.USING_FARNSWORTH, using)
    }

    /**
     * Farnsworth words per minute, for character and word spacing.
     * @return the Farnsworth wpm rate
     */
    def getFarnsworthWordsPerMinute: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.FARNSWORTH_WPM, "" + MorseTrainerPrefs.DefaultFarnsworthWordsPerMinute))
    }

    /**
     * Store the Farnsworth words per minute.
     * @return the Farnsworth wpm rate
     */
    def setFarnsworthWordsPerMinute(fwpm: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.FARNSWORTH_WPM, "" + fwpm)
    }

    /**
     * Pulse frequency in Hz.
     * @return the frequency
     */
    def getPulseFrequencyHz: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.PULSE_FREQ, "" + MorseTrainerPrefs.DefaultPulseFrequency))
    }

    /**
     * Store the pulse frequency.
     * @return the frequency in Hz
     */
    def setPulseFrequency(hz: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.PULSE_FREQ, "" + hz)
    }

    /**
     * Koch level.
     * @return the current level
     */
    def getKochLevel: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.KOCH_LEVEL, "" + MorseTrainerPrefs.DefaultKochLevel))
    }

    /**
     * Store the Koch level.
     * @return the level
     */
    def setKochLevel(lvl: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_MORSE, DefaultMorseTrainerPrefs.KOCH_LEVEL, "" + lvl)
    }

    def newCharsMoreFrequently: Boolean = iniFile.getBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.NEW_FREQUENTLY)

    def setNewCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.NEW_FREQUENTLY, more)
    }

    def missedCharsMoreFrequently: Boolean = iniFile.getBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.MISSED_FREQUENTLY)

    def setMissedCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.MISSED_FREQUENTLY, more)
    }

    def similarCharsMoreFrequently: Boolean = iniFile.getBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.SIMILAR_FREQUENTLY)

    def setSimilarCharsMoreFrequently(more: Boolean) {
        iniFile.setBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.SIMILAR_FREQUENTLY, more)
    }

    def areWordsRandomLength: Boolean = iniFile.getBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.RANDOM_LENGTH_WORDS)

    def setWordsRandomLength(random: Boolean) {
        iniFile.setBooleanValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.RANDOM_LENGTH_WORDS, random)
    }

    def getNonRandomWordLength: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.NON_RANDOM_WORD_LENGTH, "" + MorseTrainerPrefs.DefaultNonRandomWordLength))
    }

    def setNonRandomWordLength(len: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.NON_RANDOM_WORD_LENGTH, "" + len)
    }

    def getSessionLength: Int = {
        Integer.parseInt(iniFile.getValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.SESSION_LENGTH, "" + MorseTrainerPrefs.DefaultSessionLength))
    }

    def setSessionLength(len: Int) {
        iniFile.setValue(DefaultMorseTrainerPrefs.SECTION_SESSION, DefaultMorseTrainerPrefs.SESSION_LENGTH, "" + len)
    }
}
