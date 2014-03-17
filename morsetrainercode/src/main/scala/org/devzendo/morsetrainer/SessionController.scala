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
import org.devzendo.morsetrainer.prefs.MorseTrainerPrefs

object SessionController {
    private val LOGGER = LoggerFactory.getLogger(classOf[SessionController])
}

class SessionController(textAsMorseReader: TextAsMorseReader, marker: SessionMarker,
                        keyGenerator: DefaultKeyboardEventGenerator,
                        sessionView: SessionView,
                        prefs: MorseTrainerPrefs,
                        textGenerator: TextGenerator,
                        sessionMarker: SessionMarker) extends KeyboardObserver with Runnable {

    import SessionController._

    val abandon = new AtomicBoolean(false)
    val finished = new AtomicBoolean(false)
    var sessionType: SessionType = Koch
    var sessionThread: Thread = null

    keyGenerator.addKeyboardObserver(this)

    def start(sessionType: SessionType) {
        this.sessionType = sessionType
        abandon.set(false)
        finished.set(false)
        sessionThread = new Thread(this)
        sessionThread.setDaemon(true)
        sessionThread.setName(this.getClass.getSimpleName)
        sessionThread.start()
    }

    def terminate() {
        if (sessionThread != null) {
            abandon.set(true)
            sessionThread.interrupt()
        }
    }

    def eventOccurred(key: KeyboardEvent) {
        sessionMarker.keyReceived(key)
    }

    // TODO need to get abandon events

    trait Handler {
        def handle: Handler
    }

    class Countdown extends Handler {
        var time = 5
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
        val endTime = System.currentTimeMillis() + (lengthSecs * 1000)

        def handle: SessionController.this.type#Handler = {
            val secsGone = ((System.currentTimeMillis() - startTime) / 1000).toInt
            sessionView.setCurrentSessionProgressSeconds(secsGone)

            if (System.currentTimeMillis() >= endTime) {
                finished.set(true)
            } else {
                val morseChar = textGenerator.next()
                LOGGER.info("Sending '" + morseChar + "'")
                sessionMarker.charPlayed(morseChar)
                textAsMorseReader.playSynchronously(morseChar.toString)
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

            if (finished.get()) {
                sessionView.endOfSession()
            }
        } catch {
            case ie: InterruptedException => LOGGER.info("Session terminated")
        }

        LOGGER.info("Ending " + sessionType + " session")
    }
}
