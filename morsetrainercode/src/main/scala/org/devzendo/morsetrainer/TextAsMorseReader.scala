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
import java.util.concurrent.CountDownLatch

object TextAsMorseReader {
    private val LOGGER = LoggerFactory.getLogger(classOf[TextAsMorseReader])
    type TimedClipRequest = (Long, ClipRequest)
    type ClipRequestsWithTotalDuration = (List[ClipRequest], Long)
}

class TextAsMorseReader(textToMorse: TextToMorseClipRequests, clipGeneratorHolder: ClipGeneratorHolder) {
    import TextAsMorseReader._

    val q = new java.util.concurrent.LinkedBlockingQueue[ClipRequest]()

    val thread = new Thread(new Runnable() {
        def run() {
            val me: Thread = Thread.currentThread()
            LOGGER.info("Starting clip playing thread...")
            while (!me.isInterrupted) {
                val clipRequest = q.take()

                val generator = clipGeneratorHolder.clipGenerator

                val optionClip = clipRequest match {
                    case ElementSp => Some(generator.getElementSpace)
                    case CharSp => Some(generator.getCharacterSpace)
                    case WordSp => Some(generator.getWordSpace)

                    case Dah => Some(generator.getDah)
                    case Dit => Some(generator.getDit)

                    case Sync(latch) => {
                        latch.countDown()
                        None
                    }
                }

                for (c <- optionClip) {
                    c.start()
                    c.drain()
                }
            }
            LOGGER.info("Clip playing thread stopping...")
        }
    })
    thread.setName(getClass.getName)
    thread.setDaemon(true)
    thread.setPriority(Thread.MAX_PRIORITY)
    thread.start()
    
    def silence() {
        q.clear()
    }

    def clipRequestToDuration(clipRequest: ClipRequest): Long = {
        val generator = clipGeneratorHolder.clipGenerator

        val durationMs = clipRequest match {
            case ElementSp => generator.elementSpaceMs
            case CharSp => generator.characterSpaceMs
            case WordSp => generator.wordSpaceMs

            case Dah => generator.dahMs
            case Dit => generator.ditMs

            case Sync(_) => 0L
        }
        durationMs
    }

    def charSpaceClipRequestsWithTotalDuration: ClipRequestsWithTotalDuration = {
        (List[ClipRequest](CharSp), clipGeneratorHolder.clipGenerator.characterSpaceMs)
    }

    def textToClipRequestsWithTotalDuration(str: String): ClipRequestsWithTotalDuration = {
//        LOGGER.debug("translating '" + str + "' to clips and duration")
        val clipRequests = textToMorse.translateString(str)
//        LOGGER.debug("clip requests are " + clipRequests)
        val durationMss = clipRequests.map(clipRequestToDuration)
//        LOGGER.debug("clip request durations are " + durationMss)
        val totalDuration = (0L /: durationMss) (_ + _)
//        LOGGER.debug("total duration is " + totalDuration)
        val ret = (clipRequests, totalDuration)
//        LOGGER.debug("returning " + ret)
        ret
    }

    def play(str: String) {
//        LOGGER.debug("Playing string '" + str + "'")
        val clips = textToClipRequestsWithTotalDuration(str)._1
        play(clips)
    }

    def play(req: ClipRequest) {
//        LOGGER.debug("Playing clip request '" + req + "'")
        play(List(req))
    }

    def play(clipRequests: List[ClipRequest]) {
//        LOGGER.debug("Playing clip requests '" + clipRequests + "'")
        clipRequests.foreach( (cr: ClipRequest) => {
            q.put(cr)
        })
    }

    def playSynchronously(clipRequests: List[ClipRequest]) {
        val latch = new CountDownLatch(1)
        play(clipRequests)
        play(new Sync(latch))
        latch.await()
    }

    def sync() {
        val latch = new CountDownLatch(1)
        play(new Sync(latch))
        latch.await()
    }
}