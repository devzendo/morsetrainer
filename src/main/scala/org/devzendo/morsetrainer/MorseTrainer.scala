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

import javax.sound.sampled._

object MorseTrainer {

    /**
     * @param args the command line arguments.
     */
    def main(args: Array[String]) {
        new MorseTrainer()
    }
}

class MorseTrainer() {

    val clipGen = new ClipGenerator(18, 18, 600)
    play(clipGen.getElementSpace) // to get over initial click

    for (i <- 0 to 8) {
        play(clipGen.getDit)
        play(clipGen.getElementSpace)
        play(clipGen.getDah)
        play(clipGen.getElementSpace)
        play(clipGen.getDit)
        play(clipGen.getCharacterSpace)

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

    }

    def play(clip: Clip) {
        clip.start()
        clip.drain()
    }
}
