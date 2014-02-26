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

object TextAsMorseReader {
    private val LOGGER = LoggerFactory.getLogger(classOf[TextAsMorseReader])
}

class TextAsMorseReader(textToMorse: TextToMorse, clipGeneratorHolder: ClipGeneratorHolder) {
    import TextAsMorseReader._

    class ClipRequestPlayer(clipGeneratorHolder: ClipGeneratorHolder) {
        def play(clipRequest: ClipRequest) {
            val clip = clipRequest match {
                case ElementSp => clipGeneratorHolder.clipGenerator.getElementSpace
                case CharSp => clipGeneratorHolder.clipGenerator.getCharacterSpace
                case WordSp => clipGeneratorHolder.clipGenerator.getWordSpace

                case Dah => clipGeneratorHolder.clipGenerator.getDah
                case Dit => clipGeneratorHolder.clipGenerator.getDit
            }
            clip.start()
            clip.drain()
        }
    }

    val player = new ClipRequestPlayer(clipGeneratorHolder)
    val q = new java.util.concurrent.LinkedBlockingQueue[ClipRequest]()

    val thread = new Thread(new Runnable() {
        def run() {
            val me: Thread = Thread.currentThread()
            val name: String = me.getName
            LOGGER.info(name + " starting...")
            while (!me.isInterrupted) {
                player.play(q.take())
            }
            LOGGER.info(name + " stopping...")
        }
    })
    thread.setName(getClass.getName)
    thread.setDaemon(true)
    thread.setPriority(Thread.MAX_PRIORITY)
    thread.start()
    
    def silence() {
        q.clear()
    }

    def play(str: String) {
        val clips = textToMorse.translateString(str)
        clips.foreach( (cr: ClipRequest) => q.put(cr) )
    }
}
