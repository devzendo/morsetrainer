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
//import org.devzendo.commoncode.logging.Logging
import javax.sound.sampled._
import java.io.IOException

//import org.slf4j.bridge.SLF4JBridgeHandler
import collection.JavaConverters._
import collection.JavaConversions._


object MorseTrainer {
    private val LOGGER = LoggerFactory.getLogger(classOf[MorseTrainer])

    val SAMPLE_RATE: Int = 48000
    val TWO_PI: Double = Math.PI * 2.0

    /**
     * @param args the command line arguments.
     */
    def main(args: Array[String]) {
//        val logging = Logging.getInstance()
//        val finalArgList = logging.setupLoggingFromArgs(args.toList).asScala.toList

        //      SLF4JBridgeHandler.install()

        new MorseTrainer(/*finalArgList*/)
    }
}

class MorseTrainer(/*val argList: List[String]*/) {

//    import MorseTrainer._

//    LOGGER.info("Starting MorseTrainer")

    for (i <- 0 to 4) {

    val clip = createDit(18, 600)
    //		System.out.println("data length is " + data.length);
    // due to bug in Java Sound, explicitly exit the VM when
    // the sound has stopped.
    //		clip.addLineListener(new LineListener() {
    //			public void update(LineEvent event) {
    //				if (event.getType() == LineEvent.Type.STOP) {
    //					event.getLine().close();
    //					System.exit(0);
    //				}
    //			}
    //		});


    // play the sound clip
    //		System.out.println("one");
    clip.start()
    clip.drain()
    //		System.out.println("drained");
    Thread.sleep(500)
    //		Clip clip2 = createDit(12, 650);
    //		clip2.start();
    //		clip2.drain();
    //		Thread.sleep(2000);
    }

    private def createDit(wpm: Int, freqHz: Int): Clip = {
        // http://sv8gxc.blogspot.co.uk/2010/09/morse-code-101-in-wpm-bw-snr.html
        val ditLengthSeconds = 1.2 / wpm
        val samples: Int = (MorseTrainer.SAMPLE_RATE * ditLengthSeconds).asInstanceOf[Int]
        // must fill in sin(0) to sin(2pi) freqHz times in SAMPLE_RATE bytes

        // Create sine wave without ramp-up/down
        val out: Array[Byte] = createSine(samples, freqHz)

        // Ramp up at start and down at end
        ramp(samples, out, wpm)

        bytesToClip(out)
    }

    private def ramp(samples: Int, out: Array[Byte], wpm: Int) {
        val ditDurationSeconds = 1.2 / wpm
        val rampDuration = ditDurationSeconds / 16.0
        val rampSamples: Int = (MorseTrainer.SAMPLE_RATE * rampDuration).asInstanceOf[Int]
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
        val cycleLength = MorseTrainer.SAMPLE_RATE / freqHz
        val dCycleLength = cycleLength.asInstanceOf[Double]

        val out: Array[Byte] = new Array[Byte](samples)
        for (i <- 0 until samples) {
            val x = i % cycleLength
            val prop = x / dCycleLength
            val sinprop = prop * MorseTrainer.TWO_PI
            val value = Math.sin(sinprop)
            val iVal = (value * 127).asInstanceOf[Int] + 256
            val bVal = (iVal & 0xff).asInstanceOf[Byte]
            out(i) = bVal
        }
        out
    }

    private def bytesToClip(out: Array[Byte]): Clip = {
        val format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            MorseTrainer.SAMPLE_RATE.asInstanceOf[Float], 8, 1, 1,
            MorseTrainer.SAMPLE_RATE,
            false)
        val info = new DataLine.Info(classOf[Clip], format)
        val clip = AudioSystem.getLine(info).asInstanceOf[Clip]
        clip.open(format, out, 0, out.length)
        clip
    }

}
