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

class TestTextToMorse extends AssertionsForJUnit with MustMatchersForJUnit {

    val ttm = new TextToMorse

    @Test
    def empty() {
        ttm.translateString("") must be(List())
    }

    @Test
    def xlat_e() {
        ttm.translateString("e") must be(List(Dit))
    }

    @Test
    def xlat_a() {
        ttm.translateString("a") must be(List(Dit, ElementSp, Dah))
    }

    @Test
    def xlat_r() {
        ttm.translateString("r") must be(List(Dit, ElementSp, Dah, ElementSp, Dit))
    }

    @Test
    def xlat_ab() {
        ttm.translateString("ab") must be(List(Dit, ElementSp, Dah, CharSp, Dah, ElementSp, Dit, ElementSp, Dit, ElementSp, Dit))
    }

    @Test
    def xlat_aSpaceB() {
        ttm.translateString("a b") must be(List(Dit, ElementSp, Dah, WordSp, Dah, ElementSp, Dit, ElementSp, Dit, ElementSp, Dit))
    }
}
