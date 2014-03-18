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
import org.slf4j.LoggerFactory

class TestClipGenerator extends AssertionsForJUnit with MustMatchersForJUnit with LoggingUnittest {
    private val LOGGER = LoggerFactory.getLogger(classOf[TestClipGenerator])

    @Test
    def farnsworthSpacing() {
        val cg = new ClipGenerator(18, 12, 600)
        LOGGER.info("farnsworthSpacing")
        LOGGER.info("ditMs: " + cg.ditMs)
        LOGGER.info("dahMs: " + cg.dahMs)
        LOGGER.info("elementSpaceMs: " + cg.elementSpaceMs)
        LOGGER.info("characterSpaceMs: " + cg.characterSpaceMs)
        LOGGER.info("wordSpaceMs: " + cg.wordSpaceMs)
        cg.characterSpaceMs must be > cg.dahMs
    }

    @Test
    def nonFarnsworthSpacing() {
        val cg = new ClipGenerator(18, 18, 600)
        LOGGER.info("nonFarnsworthSpacing")
        LOGGER.info("ditMs: " + cg.ditMs)
        LOGGER.info("dahMs: " + cg.dahMs)
        LOGGER.info("elementSpaceMs: " + cg.elementSpaceMs)
        LOGGER.info("characterSpaceMs: " + cg.characterSpaceMs)
        LOGGER.info("wordSpaceMs: " + cg.wordSpaceMs)
        cg.characterSpaceMs must equal (cg.dahMs)
    }
}
