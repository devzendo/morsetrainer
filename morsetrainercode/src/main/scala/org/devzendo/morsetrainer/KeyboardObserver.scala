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

import org.devzendo.commoncode.patterns.observer.{ObserverList, ObservableEvent, Observer}
import javax.swing.{SwingWorker, JFrame}
import org.slf4j.LoggerFactory
import java.awt.event.{KeyAdapter, KeyEvent}


sealed abstract class KeyboardEvent extends ObservableEvent
case class Key(ch: Char) extends KeyboardEvent
case object Backspace extends KeyboardEvent

trait KeyboardObserver extends Observer[KeyboardEvent]

object DefaultKeyboardEventGenerator {
    private val LOGGER = LoggerFactory.getLogger(classOf[DefaultKeyboardEventGenerator])
}

class DefaultKeyboardEventGenerator {
    import DefaultKeyboardEventGenerator._

    val keyboardObservers = new ObserverList[KeyboardEvent]

    def setMainFrame(mainFrame: JFrame) {
        mainFrame.addKeyListener(new KeyAdapter() {
            override def keyTyped(e: KeyEvent) {
//                LOGGER.debug("key event: " + e)
//                LOGGER.debug("char as int " + e.getKeyChar.toInt)
                if (e.getKeyChar.toInt == 8) {
                    LOGGER.debug("Key: Backspace")
                    emit(Backspace)
                } else {
                    val lowerChar: Char = e.getKeyChar.toLower
                    if ((lowerChar >= '0' && lowerChar <= '9') ||
                        (lowerChar >= 'a' && lowerChar <= 'z') ||
                        (lowerChar == '?' || lowerChar == '/' || lowerChar == '.' ||
                         lowerChar == ',' || lowerChar == '?' || lowerChar == '=' ||
                         lowerChar == ' ')) {
                        LOGGER.debug("Key: " + lowerChar)
                        emit(Key(lowerChar))
                    }
                }
            }

            private def emit(key: KeyboardEvent) {
                new SwingWorker[Unit, AnyRef]() {
                    def doInBackground(): Unit = {
                        keyboardObservers.eventOccurred(key)
                    }
                }.execute()
            }
        })
        mainFrame.requestFocusInWindow()
        mainFrame.setFocusable(true)
    }

    def addKeyboardObserver(l: KeyboardObserver) {
        keyboardObservers.addObserver(l)
    }

    def removeKeyboardObserver(l: KeyboardObserver) {
        keyboardObservers.removeListener(l)
    }
}