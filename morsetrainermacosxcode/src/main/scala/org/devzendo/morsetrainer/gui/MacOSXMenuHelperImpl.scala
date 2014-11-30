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

import com.apple.eawt._

import org.devzendo.commonapp.gui.menu._
import com.apple.eawt.AppEvent.{QuitEvent, PreferencesEvent, AboutEvent}
import org.slf4j.LoggerFactory

object MacOSXMenuImplHelper {
    private val LOGGER = LoggerFactory.getLogger(classOf[MacOSXMenuImplHelper])

}

class MacOSXMenuImplHelper(val wiring: MenuWiring) {
    MacOSXMenuImplHelper.LOGGER.info("Initialising MacOSX-specific menu")
    val macApp = Application.getApplication

    macApp.setAboutHandler(new AboutHandler() {
        def handleAbout(ae: AboutEvent) {
            MacOSXMenuImplHelper.LOGGER.info("Handling About menu event")
            wiring.triggerActionListener(MorseMenuIdentifiersS.HELP_ABOUT)
        }
    } )

    macApp.setPreferencesHandler(new PreferencesHandler {
        def handlePreferences(pe: PreferencesEvent) {
            MacOSXMenuImplHelper.LOGGER.info("Handling Preferences menu event")
            wiring.triggerActionListener(MorseMenuIdentifiersS.TOOLS_OPTIONS)
        }
    })

    macApp.setQuitHandler(new QuitHandler {
        def handleQuitRequestWith(pe: QuitEvent, pr: QuitResponse) {
            MacOSXMenuImplHelper.LOGGER.info("Handling Quit menu event " + pe)
            wiring.triggerActionListener(MorseMenuIdentifiersS.FILE_EXIT)
        }
    })
}
