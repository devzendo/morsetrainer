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

package org.devzendo.morsetrainer.gui

import org.devzendo.commonapp.service.{ServiceManagerProxy, Service}
import org.devzendo.commonapp.spring.springloader.SpringLoader
import org.devzendo.commonapp.gui.menu.{MenuIdentifier, MenuWiring}
import org.slf4j.LoggerFactory
import java.awt.event.ActionListener
import org.devzendo.commonapp.gui.GUIUtils

object MenuInitialisingService {
    private val LOGGER = LoggerFactory.getLogger(classOf[MenuInitialisingService])
}

class MenuInitialisingService(springLoader: SpringLoader, menu: Menu, menuWiring: MenuWiring) extends Service {
    import MenuInitialisingService._

    def startup(proxy: ServiceManagerProxy) {
        GUIUtils.runOnEventThread(new Runnable() {
            def run() {
                LOGGER.info("Initialising menu")

                loadAndWire(MorseMenuIdentifiersS.TOOLS_OPTIONS)
                loadAndWire(MorseMenuIdentifiersS.HELP_ABOUT)

                LOGGER.info("Menu initialised")
            }
        })
    }

    private def loadAndWire(menuIdentifier: MenuIdentifier) {
        LOGGER.info(String.format("Loading ActionListener '%s'", menuIdentifier.toString))
        menuWiring.setActionListener(menuIdentifier, springLoader.getBean("menuAL" + menuIdentifier.toString, classOf[ActionListener]))
    }

    def prepareForShutdown() {}

    def shutdown() {}
}
