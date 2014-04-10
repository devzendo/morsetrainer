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
import org.easymock.EasyMock
import org.devzendo.morsetrainer.prefs.{RecognitionRate, RecognitionRatePersister, MorseTrainerPrefs}
import org.devzendo.morsetrainer.Morse._

object TestRecognitionRatePersister {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestRecognitionRatePersister])
}

class TestRecognitionRatePersister extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    import TestRecognitionRatePersister._

    val rates: Map[MorseChar, RecognitionRate] = Map(
        'K' -> RecognitionRate(1, 10),
        'M' -> RecognitionRate(2, 10),
        'R' -> RecognitionRate(10, 10),
        'S' -> RecognitionRate(10, 10),
        'U' -> RecognitionRate(0, 10),
        'A' -> RecognitionRate(3, 10),
        'P' -> RecognitionRate(4, 10),
        'T' -> RecognitionRate(1, 10),
        'L' -> RecognitionRate(2, 10),
        'O' -> RecognitionRate(8, 10),
        'W' -> RecognitionRate(3, 10),
        'I' -> RecognitionRate(2, 10)
    )
    def mockPrefsAreInitialisedWithoutReplay(): MorseTrainerPrefs = {
        val mockPrefs = EasyMock.createMock(classOf[MorseTrainerPrefs])
        mockPrefs.getCharacterRecognitionRates
        EasyMock.expectLastCall().andReturn(rates)

        val initialisedRates: Map[MorseChar, RecognitionRate] = Map(
            ' ' -> RecognitionRate(0, 0), // SPACE is added to the initialised recognition rates
            'A' -> RecognitionRate(3, 10),
            'B' -> RecognitionRate(0, 0),
            'C' -> RecognitionRate(0, 0),
            'D' -> RecognitionRate(0, 0),
            'E' -> RecognitionRate(0, 0),
            'F' -> RecognitionRate(0, 0),
            'G' -> RecognitionRate(0, 0),
            'H' -> RecognitionRate(0, 0),
            'I' -> RecognitionRate(2, 10),
            'J' -> RecognitionRate(0, 0),
            'K' -> RecognitionRate(1, 10),
            'L' -> RecognitionRate(2, 10),
            'M' -> RecognitionRate(2, 10),
            'N' -> RecognitionRate(0, 0),
            'O' -> RecognitionRate(8, 10),
            'P' -> RecognitionRate(4, 10),
            'Q' -> RecognitionRate(0, 0),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'T' -> RecognitionRate(1, 10),
            'U' -> RecognitionRate(0, 10),
            'V' -> RecognitionRate(0, 0),
            'W' -> RecognitionRate(3, 10),
            'X' -> RecognitionRate(0, 0),
            'Y' -> RecognitionRate(0, 0),
            'Z' -> RecognitionRate(0, 0),
            '1' -> RecognitionRate(0, 0),
            '2' -> RecognitionRate(0, 0),
            '3' -> RecognitionRate(0, 0),
            '4' -> RecognitionRate(0, 0),
            '5' -> RecognitionRate(0, 0),
            '6' -> RecognitionRate(0, 0),
            '7' -> RecognitionRate(0, 0),
            '8' -> RecognitionRate(0, 0),
            '9' -> RecognitionRate(0, 0),
            '0' -> RecognitionRate(0, 0),
            '?' -> RecognitionRate(0, 0),
            '/' -> RecognitionRate(0, 0),
            '.' -> RecognitionRate(0, 0),
            ',' -> RecognitionRate(0, 0),
            '=' -> RecognitionRate(0, 0),
            '+' -> RecognitionRate(0, 0)
        )
        mockPrefs.setCharacterRecognitionRates(EasyMock.eq(initialisedRates))
        mockPrefs
    }

    def mockPrefsAreInitialised(): MorseTrainerPrefs = {
        val mockPrefs = mockPrefsAreInitialisedWithoutReplay()
        EasyMock.replay(mockPrefs)
        mockPrefs
    }

    @Test
    def persisterInitialises() {
        val mockPrefs = mockPrefsAreInitialised()

        new RecognitionRatePersister(mockPrefs)

        EasyMock.verify(mockPrefs)
    }

    @Test
    def initialRecognitionRateIsCorrect() {
        val mockPrefs = mockPrefsAreInitialised()

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.getRecognitionRates(Set('A', 'U', 'R', 'G')) must be(Map(
            'A' -> RecognitionRate(3, 10),
            'U' -> RecognitionRate(0, 10),
            'R' -> RecognitionRate(10, 10),
            'G' -> RecognitionRate(0, 0)
        ))

        EasyMock.verify(mockPrefs)
    }

    @Test
    def charPlayedKeyReceivedAndTheyMatch() {
        val mockPrefs = mockPrefsAreInitialised()

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.charPlayed('A')
        persister.keyReceived(Key('A'))

        persister.getRecognitionRates(Set('A')) must be(Map('A' -> RecognitionRate(4, 11)))

        EasyMock.verify(mockPrefs)
    }

    @Test
    def charPlayedKeyReceivedAndTheyDontMatch() {
        val mockPrefs = mockPrefsAreInitialised()

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.charPlayed('A')
        persister.keyReceived(Key('N'))

        persister.getRecognitionRates(Set('A')) must be(Map('A' -> RecognitionRate(3, 11)))

        EasyMock.verify(mockPrefs)
    }

    @Test
    def complexRealism() {
        val mockPrefs = mockPrefsAreInitialised()

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.charPlayed('V') // deletion
        persister.charPlayed('A') // mutation
        persister.keyReceived(Key('N'))
        persister.charPlayed('U')
        persister.keyReceived(Key('U'))
        persister.charPlayed('U')
        persister.keyReceived(Key('U'))
        persister.charPlayed('G')
        persister.keyReceived(Key('G'))

        persister.getRecognitionRates(Set('V', 'A', 'U', 'R', 'G')) must be(Map(
            'V' -> RecognitionRate(0, 1),
            'A' -> RecognitionRate(3, 11),
            'U' -> RecognitionRate(2, 12),
            'R' -> RecognitionRate(10, 10),
            'G' -> RecognitionRate(1, 1)
        ))

        EasyMock.verify(mockPrefs)
    }

    @Test
    def backspaceDeletesLastKey() {
        val mockPrefs = mockPrefsAreInitialised()

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.getRecognitionRates(Set('N')) must be(Map('N' -> RecognitionRate(0, 0)))

        persister.charPlayed('N')
        persister.keyReceived(Key('N'))

        persister.getRecognitionRates(Set('N')) must be(Map('N' -> RecognitionRate(1, 1)))

        persister.keyReceived(Backspace)
        persister.keyReceived(Backspace) // doesn't crash when keys emptied :)

        persister.getRecognitionRates(Set('N')) must be(Map('N' -> RecognitionRate(0, 1)))

        EasyMock.verify(mockPrefs)
    }

    @Test
    def persistSavesAllChanges() {
        val mockPrefs = mockPrefsAreInitialisedWithoutReplay()

        val expectedRatesToPersist = Map(
            'A' -> RecognitionRate(4, 11), // only this has changed
            'B' -> RecognitionRate(0, 0),
            'C' -> RecognitionRate(0, 0),
            'D' -> RecognitionRate(0, 0),
            'E' -> RecognitionRate(0, 0),
            'F' -> RecognitionRate(0, 0),
            'G' -> RecognitionRate(0, 0),
            'H' -> RecognitionRate(0, 0),
            'I' -> RecognitionRate(2, 10),
            'J' -> RecognitionRate(0, 0),
            'K' -> RecognitionRate(1, 10),
            'L' -> RecognitionRate(2, 10),
            'M' -> RecognitionRate(2, 10),
            'N' -> RecognitionRate(0, 0),
            'O' -> RecognitionRate(8, 10),
            'P' -> RecognitionRate(4, 10),
            'Q' -> RecognitionRate(0, 0),
            'R' -> RecognitionRate(10, 10),
            'S' -> RecognitionRate(10, 10),
            'T' -> RecognitionRate(1, 10),
            'U' -> RecognitionRate(0, 10),
            'V' -> RecognitionRate(0, 0),
            'W' -> RecognitionRate(3, 10),
            'X' -> RecognitionRate(0, 0),
            'Y' -> RecognitionRate(0, 0),
            'Z' -> RecognitionRate(0, 0),
            '1' -> RecognitionRate(0, 0),
            '2' -> RecognitionRate(0, 0),
            '3' -> RecognitionRate(0, 0),
            '4' -> RecognitionRate(0, 0),
            '5' -> RecognitionRate(0, 0),
            '6' -> RecognitionRate(0, 0),
            '7' -> RecognitionRate(0, 0),
            '8' -> RecognitionRate(0, 0),
            '9' -> RecognitionRate(0, 0),
            '0' -> RecognitionRate(0, 0),
            '?' -> RecognitionRate(0, 0),
            '/' -> RecognitionRate(0, 0),
            '.' -> RecognitionRate(0, 0),
            ',' -> RecognitionRate(0, 0),
            '=' -> RecognitionRate(0, 0),
            '+' -> RecognitionRate(0, 0)
        )
        mockPrefs.setCharacterRecognitionRates(expectedRatesToPersist)
        EasyMock.replay(mockPrefs)

        val persister = new RecognitionRatePersister(mockPrefs)

        persister.charPlayed('A')
        persister.keyReceived(Key('A'))

        persister.persist()

        EasyMock.verify(mockPrefs)
    }
}