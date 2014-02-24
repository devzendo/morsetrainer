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

object ClipGeneratorHolder {
    private val LOGGER = LoggerFactory.getLogger(classOf[ClipGeneratorHolder])
}

class ClipGeneratorHolder(var wpm: Int, var usingFarnsworth: Boolean, var fwpm: Int, var freq: Int) {
    import ClipGeneratorHolder._

    var dirty = false
    var cachedClipGenerator = createClipGenerator

    private def createClipGenerator: ClipGenerator = {
        val actualFwpm: Int = if (usingFarnsworth) fwpm else wpm
        LOGGER.info("Creating ClipGenerator for WPM " + wpm + ", using Farnsworth? " + usingFarnsworth + ", Farnsworth WPM " + actualFwpm + ", freq " + freq)
        new ClipGenerator(wpm, actualFwpm, freq)
    }

    def setWordsPerMinute(newWpm: Int) {
        synchronized {
            if (newWpm != wpm) {
                dirty = true
                wpm = newWpm
            }
        }
    }

    def setUsingFarnsworth(using: Boolean) {
        synchronized {
            if (usingFarnsworth != using) {
                dirty = true
                usingFarnsworth = using
            }
        }
    }

    def setFarnsworthWordsPerMinute(newFWpm: Int) {
        synchronized {
            if (newFWpm != fwpm) {
                dirty = true
                fwpm = newFWpm
            }
        }
    }

    def setPulseFrequency(newFreq: Int) {
        synchronized {
            if (newFreq != freq) {
                dirty = true
                freq = newFreq
            }
        }
    }

    def clipGenerator: ClipGenerator = {
        synchronized {
            if (dirty) {
                dirty = false
                cachedClipGenerator = createClipGenerator
            }
            cachedClipGenerator
        }
    }
}

