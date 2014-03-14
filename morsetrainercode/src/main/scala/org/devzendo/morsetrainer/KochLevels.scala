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

import org.devzendo.morsetrainer.Morse.MorseChar

object KochLevels {
    val levels = Array(
        "KM",
        "R",
        "S",
        "U",
        "A",
        "P",
        "T",
        "L",
        "O",
        "W",
        "I",
        ".",
        "N",
        "J",
        "E",
        "F",
        "0",
        "Y",
        ",",
        "V",
        "G",
        "5",
        "/",
        "Q",
        "9",
        "Z",
        "H",
        "3",
        "8",
        "B",
        "?",
        "4",
        "2",
        "7",
        "C",
        "1",
        "D",
        "6",
        "X",
        "=",
        "+"
    )

    def morseCharsSubArrayForLevel(level: Int): Array[String] = {
        if (level < 2) {
            throw new IllegalArgumentException("Koch level must be >= 2")
        }

        // Level starts at 2 - for the two chars KM.
        levels.take(Math.min(level - 1, levels.length))
    }

    def morseCharsForLevel(level: Int): Set[MorseChar] = {
        if (level < 2) {
            throw new IllegalArgumentException("Koch level must be >= 2")
        }

        // Level starts at 2 - for the two chars KM.
        (for {
            i <- 0 to Math.min(level - 2, levels.length - 1)
            ch <- levels(i)
        } yield ch).toSet[MorseChar]
    }
}
