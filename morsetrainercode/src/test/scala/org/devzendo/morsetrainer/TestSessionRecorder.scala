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
import org.junit.Test
import org.easymock.EasyMock

class TestSessionRecorder extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {

    val recorder = new SessionRecorder

    @Test
    def sendACharAndGetNotified() {
        val mockObserver = EasyMock.createMock(classOf[SessionStateObserver])
        mockObserver.eventOccurred(SentCharacters("A"))
        mockObserver.eventOccurred(SentCharacters("AB"))
        mockObserver.eventOccurred(SentCharacters("ABC"))
        EasyMock.replay(mockObserver)

        recorder.addSessionStateObserver(mockObserver)
        recorder.charPlayed('A')
        recorder.charPlayed('B')
        recorder.charPlayed('C')

        EasyMock.verify(mockObserver)
    }

    @Test
    def receiveAKeyAndGetNotified() {
        val mockObserver = EasyMock.createMock(classOf[SessionStateObserver])
        mockObserver.eventOccurred(ReceivedKeys("A"))
        mockObserver.eventOccurred(ReceivedKeys("AB"))
        mockObserver.eventOccurred(ReceivedKeys("ABC"))
        EasyMock.replay(mockObserver)

        recorder.addSessionStateObserver(mockObserver)
        recorder.keyReceived(Key('A'))
        recorder.keyReceived(Key('B'))
        recorder.keyReceived(Key('C'))

        EasyMock.verify(mockObserver)
    }

    @Test
    def backspaceRemovesLastKeyKey() {
        val mockObserver = EasyMock.createMock(classOf[SessionStateObserver])
        mockObserver.eventOccurred(ReceivedKeys("A"))
        mockObserver.eventOccurred(ReceivedKeys("AB"))
        mockObserver.eventOccurred(ReceivedKeys("ABC"))
        mockObserver.eventOccurred(ReceivedKeys("AB"))
        mockObserver.eventOccurred(ReceivedKeys("ABD"))
        EasyMock.replay(mockObserver)

        recorder.addSessionStateObserver(mockObserver)
        recorder.keyReceived(Key('A'))
        recorder.keyReceived(Key('B'))
        recorder.keyReceived(Key('C'))
        recorder.keyReceived(Backspace)
        recorder.keyReceived(Key('D'))

        EasyMock.verify(mockObserver)
    }

    @Test
    def backspaceDoesNotSendEventWhenEmpty() {
        val mockObserver = EasyMock.createMock(classOf[SessionStateObserver])
        mockObserver.eventOccurred(ReceivedKeys("A"))
        mockObserver.eventOccurred(ReceivedKeys(""))
        EasyMock.replay(mockObserver)

        recorder.addSessionStateObserver(mockObserver)
        recorder.keyReceived(Key('A'))  // Get "A"
        recorder.keyReceived(Backspace) // Get ""
        recorder.keyReceived(Backspace) // Not reported, not testable other than
        recorder.keyReceived(Backspace) // only getting two events.

        EasyMock.verify(mockObserver)
    }

    @Test
    def resetEmpties() {
        val mockObserver = EasyMock.createMock(classOf[SessionStateObserver])
        mockObserver.eventOccurred(ReceivedKeys("A"))
        mockObserver.eventOccurred(ReceivedKeys("Z"))
        EasyMock.replay(mockObserver)

        recorder.addSessionStateObserver(mockObserver)
        recorder.keyReceived(Key('A'))
        recorder.reset()
        recorder.keyReceived(Key('Z'))

        EasyMock.verify(mockObserver)
    }
}
