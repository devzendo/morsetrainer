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

class TestEditMatcher extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {

    @Test
    def empty() {
        new EditMatcher("", "").edits() must be(List())
    }

    @Test
    def perfect() {
        new EditMatcher("abcd", "abcd").edits() must be(List(Match('a'), Match('b'), Match('c'), Match('d')))
    }

    @Test
    def hatTape() {
        new EditMatcher("tape", "hat").edits() must be(List(Mutation('t'), Match('a'), Deletion('p'), Mutation('e')))
    }

    @Test
    def deletion() {
        new EditMatcher("abcd", "bcd").edits() must be(List(Deletion('a'), Match('b'), Match('c'), Match('d')))
    }

    @Test
    def deletion2() {
        new EditMatcher("abcd", "aabcd").edits() must be(List(Deletion('a'), Match('a'), Match('b'), Match('c'), Match('d')))
    }

    @Test
    def deletionInside() {
        new EditMatcher("abcd", "ad").edits() must be(List(Match('a'), Deletion('b'), Deletion('c'), Match('d')))
    }

    @Test
    def mutation() {
        new EditMatcher("abcd", "nbcd").edits() must be(List(Mutation('a'), Match('b'), Match('c'), Match('d')))
    }

    @Test
    def deletionsAtEnd() {
        new EditMatcher("abcd", "abcdefg").edits() must be(List(
            Match('a'), Match('b'), Match('c'), Match('d'),
            Deletion('e'), Deletion('f'), Deletion('g')
        ))
    }

    @Test
    def deletionsAtEnd2() {
        new EditMatcher("abcdefg", "abcd").edits() must be(List(
            Match('a'), Match('b'), Match('c'), Match('d'),
            Deletion('e'), Deletion('f'), Deletion('g')
        ))
    }
}
