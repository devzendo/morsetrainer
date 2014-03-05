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

object Morse {
    type WordString = String
    type MorseString = String

    val charMap = Map[Char, MorseString](
        'a' -> ".-",
        'b' -> "-...",
        'c' -> "-.-.",
        'd' -> "-..",
        'e' -> ".",
        'f' -> ".-..",
        'g' -> "--.",
        'h' -> "....",
        'i' -> "..",
        'j' -> ".---",
        'k' -> "-.-",
        'l' -> ".-..",
        'm' -> "--",
        'n' -> "-.",
        'o' -> "---",
        'p' -> ".--.",
        'q' -> "--.-",
        'r' -> ".-.",
        's' -> "...",
        't' -> "-",
        'u' -> "..-",
        'v' -> "...-",
        'w' -> ".--",
        'x' -> "-..-",
        'z' -> "--..",
        '1' -> ".----",
        '2' -> "..---",
        '3' -> "...--",
        '4' -> "....-",
        '5' -> ".....",
        '6' -> "-....",
        '7' -> "--...",
        '8' -> "---..",
        '9' -> "----.",
        '0' -> "-----",
        '?' -> "..--..",
        '/' -> "-..-.",
        '.' -> ".-.-.-",
        ',' -> "--..--",
        '=' -> "-...-",
        '+' -> "-...-"
    )

    def chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890?/.,=+"
}
