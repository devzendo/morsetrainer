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

import org.devzendo.morsetrainer.Morse._
import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory
import org.devzendo.commoncode.patterns.observer.{ObserverList, Observer, ObservableEvent}

object SessionRecorder {
    private val LOGGER = LoggerFactory.getLogger(classOf[SessionRecorder])
}

sealed abstract class SessionStateEvent extends ObservableEvent
case class ReceivedKeys(keys: String) extends SessionStateEvent
case class SentCharacters(chars: String) extends SessionStateEvent

trait SessionStateObserver extends Observer[SessionStateEvent]

class SessionRecorder {
    import SessionRecorder._

    val sessionStateObservers = new ObserverList[SessionStateEvent]

    var typedKeys = ArrayBuffer[MorseChar]()
    val sentChars = ArrayBuffer[MorseChar]()

    def reset() {
        LOGGER.debug("Resetting")
        typedKeys.clear()
        sentChars.clear()
    }

    def addSessionStateObserver(l: SessionStateObserver) {
        sessionStateObservers.addObserver(l)
    }

    def removeSessionStateObserver(l: SessionStateObserver) {
        sessionStateObservers.removeListener(l)
    }

    def keyReceived(key: KeyboardEvent) {
        LOGGER.info("Received key: " + key)
        key match {
            case Backspace => {
                if (!typedKeys.isEmpty) {
                    typedKeys = typedKeys.init
                    sessionStateObservers.eventOccurred(ReceivedKeys(new String(typedKeys.toArray)))
                }
            }
            case Key(ch: Char) =>
                typedKeys += ch
                sessionStateObservers.eventOccurred(ReceivedKeys(new String(typedKeys.toArray)))
        }
    }

    def charPlayed(ch: MorseChar) {
        LOGGER.info("Played: '" + ch + "'")
        sentChars += ch
        sessionStateObservers.eventOccurred(SentCharacters(new String(sentChars.toArray)))
    }
}
