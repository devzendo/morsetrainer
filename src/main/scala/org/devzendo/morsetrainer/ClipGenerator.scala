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

import javax.sound.sampled.{AudioSystem, DataLine, AudioFormat, Clip}

object ClipGenerator {
    val SAMPLE_RATE: Int = 48000
    val TWO_PI: Double = Math.PI * 2.0
}

class ClipGenerator(var wpm: Int, var fwpm: Int, var freqHz: Int) {

    private var ditDurationSeconds = 0.0
    private var dit: Array[Byte] = null
    private var dah: Array[Byte] = null
    private var elementSpace: Array[Byte] = null
    private var characterSpace: Array[Byte] = null
    private var wordSpace: Array[Byte] = null

    initialise()
    initialiseSpacing()

    private def initialise() {
        // http://sv8gxc.blogspot.co.uk/2010/09/morse-code-101-in-wpm-bw-snr.html
        ditDurationSeconds = wpmToSeconds(wpm)
        dit = createPulse(ditDurationSeconds)
        dah = createPulse(ditDurationSeconds * 3.0)
        elementSpace = createSilence(ditDurationSeconds)
    }

    private def initialiseSpacing() {
        characterSpace = createSilence(wpmToSeconds(fwpm) * 3.0)
        wordSpace = createSilence(wpmToSeconds(fwpm) * 7.0)
    }

    def getDit = bytesToClip(dit)
    def getDah = bytesToClip(dah)
    def getElementSpace = bytesToClip(elementSpace)
    def getCharacterSpace = bytesToClip(characterSpace)
    def getWordSpace = bytesToClip(wordSpace)

    def setWpm(w: Int) {
        wpm = w
        initialise()
    }

    def setFarnsworthWpm(w: Int) {
        fwpm = w
        initialiseSpacing()
    }

    def setFrequency(hz: Int) {
        freqHz = hz
        initialise()
    }

    private def wpmToSeconds(wpm: Int) = 1.2 / wpm

    private def createPulse(durationSeconds: Double): Array[Byte] = {
        val samples: Int = (ClipGenerator.SAMPLE_RATE * durationSeconds).asInstanceOf[Int]
        // must fill in sin(0) to sin(2pi) freqHz times in SAMPLE_RATE bytes

        // Create sine wave without ramp-up/down
        val out: Array[Byte] = createSine(samples, freqHz)

        // Ramp up at start and down at end
        ramp(samples, out, wpm)
        out
    }

    private def createSilence(durationSeconds: Double): Array[Byte] = {
        val samples: Int = (ClipGenerator.SAMPLE_RATE * durationSeconds).asInstanceOf[Int]
        val out: Array[Byte] = new Array[Byte](samples)
        out
    }

    private def ramp(samples: Int, out: Array[Byte], wpm: Int) {
        val rampDurationSeconds = ditDurationSeconds / 8.0   // should probably be based on the sample rate?
        val rampSamples: Int = (ClipGenerator.SAMPLE_RATE * rampDurationSeconds).asInstanceOf[Int]
        val dRampSamples = rampSamples.asInstanceOf[Double]
        for (i <- 0 to rampSamples) {
            val d = i / dRampSamples
            val v = out(i) * d
            out(i) = v.asInstanceOf[Byte]
        }
        for (i <- (rampSamples - 1) until (0, -1)) {
            val d = i / dRampSamples
            val v = out(samples - i) * d
            out(samples - i) = v.asInstanceOf[Byte]
        }
    }

    private def createSine(samples: Int, freqHz: Int): Array[Byte] = {
        val cycleLength = ClipGenerator.SAMPLE_RATE / freqHz
        val dCycleLength = cycleLength.asInstanceOf[Double]

        val out: Array[Byte] = new Array[Byte](samples)
        for (i <- 0 until samples) {
            val x = i % cycleLength
            val prop = x / dCycleLength
            val sinprop = prop * ClipGenerator.TWO_PI
            val value = Math.sin(sinprop)
            val iVal = (value * 127).asInstanceOf[Int] + 256
            val bVal = (iVal & 0xff).asInstanceOf[Byte]
            out(i) = bVal
        }
        out
    }

    private def bytesToClip(out: Array[Byte]): Clip = {
        val format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            ClipGenerator.SAMPLE_RATE.asInstanceOf[Float], 8, 1, 1,
            ClipGenerator.SAMPLE_RATE,
            false)
        val info = new DataLine.Info(classOf[Clip], format)
        val clip = AudioSystem.getLine(info).asInstanceOf[Clip]
        clip.open(format, out, 0, out.length)
        clip.setFramePosition(0)
        clip

    }

}
