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
import java.util.concurrent.atomic.AtomicBoolean
import org.devzendo.morsetrainer.prefs.{RecognitionRatePersister, MorseTrainerPrefs}
import org.devzendo.morsetrainer.Morse.MorseChar

object SessionController {
    private val LOGGER = LoggerFactory.getLogger(classOf[SessionController])
}

class SessionController(textAsMorseReader: TextAsMorseReader,
                        keyGenerator: DefaultKeyboardEventGenerator,
                        sessionView: SessionView,
                        prefs: MorseTrainerPrefs,
                        textGenerator: TextGenerator,
                        textSpacingIterator: TextSpacingIterator,
                        recognitionRatePersister: RecognitionRatePersister,
                        sessionRecorder: SessionRecorder) extends KeyboardObserver with Runnable {

    import SessionController._

    val abandon = new AtomicBoolean(false)
    val finished = new AtomicBoolean(false)
    var sessionType: SessionType = Koch
    var sessionThread: Thread = null

    keyGenerator.addKeyboardObserver(this)

    def start(sessionType: SessionType, freeStyleSelected: Set[MorseChar]) {
        this.sessionType = sessionType
        textGenerator.start(sessionType, freeStyleSelected)
        textSpacingIterator.reset()
        recognitionRatePersister.reset()
        sessionRecorder.reset()
        sessionView.setSessionType(sessionType)
        abandon.set(false)
        finished.set(false)
        sessionThread = new Thread(this)
        sessionThread.setDaemon(true)
        sessionThread.setName(this.getClass.getSimpleName)
        sessionThread.start()
    }

    def terminate() {
        LOGGER.info("Terminating " + sessionType + " session")
        if (sessionThread != null) {
            abandon.set(true)
            sessionThread.interrupt()
        }
        recognitionRatePersister.reset()
        textSpacingIterator.reset()
        LOGGER.info("Session terminated")
    }

    def endOfSession() {
        LOGGER.info("Reached end of " + sessionType + " session")
        recognitionRatePersister.persist()
        sessionView.endOfSession()
        LOGGER.info("Session ended")
    }

    def eventOccurred(key: KeyboardEvent) {
        recognitionRatePersister.keyReceived(key)
        sessionRecorder.keyReceived(key)
    }

    trait Handler {
        def handle: Handler
    }

    class Countdown extends Handler {
        var time = 3
        def handle: SessionController.this.type#Handler = {
            Thread.sleep(1000)
            time -= 1
            if (time > 0) {
                sessionView.setCountdownSeconds(time)
                this
            } else if (time == 0) {
                sessionView.clearCountdown()
                this
            } else {
                new RunSession()
            }
        }
    }

    class RunSession extends Handler {
        val lengthSecs = prefs.getSessionLength * 60
        val startTime = System.currentTimeMillis()
        val lengthMs = lengthSecs * 1000L
        val endTime = System.currentTimeMillis() + lengthMs
        var durationPlayed = 0L

        def handle: SessionController.this.type#Handler = {
            val secsGone = ((System.currentTimeMillis() - startTime) / 1000).toInt
            sessionView.setCurrentSessionProgressSeconds(secsGone)

            if (System.currentTimeMillis() >= endTime) {
                LOGGER.info("Session end time reached")
                finished.set(true)
            } else {
                val (optionMorseChar, clipRequests, duration) = textSpacingIterator.next()
                if (durationPlayed + duration >= lengthMs) {
                    LOGGER.info("Not enough time to send final sequence")
                    finished.set(true)
                } else {
                    textAsMorseReader.play(clipRequests)
                    durationPlayed += duration

                    for (morseChar <- optionMorseChar) {
                        LOGGER.info("Sending '" + morseChar + "'")
                        recognitionRatePersister.charPlayed(morseChar)
                        sessionRecorder.charPlayed(morseChar)

                        // TODO if space played, recalculate the TextGenerator's
                        // assessment of what needs sending (based on how wrong
                        // the SessionMarker thinks you've been). Don't want to
                        // do that on every char - too expensive?
                    }

                    textAsMorseReader.sync()
                }
            }
            this
        }
    }

    def run() {
        LOGGER.info("Starting a " + sessionType + " session")

        try {
            var handler: Handler = new Countdown()
            while(!abandon.get() && !finished.get()) {
                handler = handler.handle
            }

            if (finished.get() && !abandon.get()) {
                endOfSession()
            }
        } catch {
            case ie: InterruptedException => LOGGER.info("Session terminated")
        }

        LOGGER.info("Ending " + sessionType + " session")
    }
}
