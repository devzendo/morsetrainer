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

import org.devzendo.commoncode.string.StringUtils
import org.devzendo.commonapp.spring.springloader.{SpringLoader, SpringLoaderFactory}
import scala.collection.JavaConverters._
import org.slf4j.LoggerFactory

object SpringLoaderInitialiser {
    private val LOGGER = LoggerFactory.getLogger(classOf[SpringLoaderInitialiser])
}

/**
 * Initialise the SpringLoader with the Application Contexts
 * @param applicationContexts the application context files, as resource paths
 */
class SpringLoaderInitialiser(applicationContexts: List[String]) {

    // Now load up Spring...
    val startSpring = System.currentTimeMillis()
    SpringLoaderInitialiser.LOGGER.debug("Initialising SpringLoader with context files: " + applicationContexts)
    val springLoader: SpringLoader = SpringLoaderFactory.initialise(applicationContexts.asJava)
    val stopSpring = System.currentTimeMillis()
    val springElapsed = stopSpring - startSpring
    SpringLoaderInitialiser.LOGGER.debug("SpringLoader initialised in " + StringUtils.translateTimeDuration(springElapsed))

    /**
     * @return the SpringLoader
     */
    def getSpringLoader: SpringLoader = springLoader
}

