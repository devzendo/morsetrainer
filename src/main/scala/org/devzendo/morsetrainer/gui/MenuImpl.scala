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

import javax.swing.JMenuBar
import javax.swing.SwingUtilities


import org.devzendo.commonapp.gui.menu._
import org.devzendo.commoncode.os.OSTypeDetect
import org.slf4j.LoggerFactory

object MenuImpl {
    private val LOGGER = LoggerFactory.getLogger(classOf[MenuImpl])
}

class MenuImpl(val wiring: MenuWiring, val fileMenu: FileMenu) extends Menu {
    val osType = OSTypeDetect.getInstance().getOSType
    var menuBar: JMenuBar = null
    MenuImpl.LOGGER.debug("Starting menu construction")

    def initialise() {
        MenuImpl.LOGGER.debug("Menu has detected OS type as " + osType)
        assert(SwingUtilities.isEventDispatchThread)
        menuBar = new JMenuBar()
        // now we could get Spring to do this; a project for a wet afternoon..
        val helperClassName = osType.name() + "MenuImplHelper"
        val fullHelperClassName = getClass.getPackage.getName + "." + helperClassName
        try {
            MenuImpl.LOGGER.info("Trying to load platform-specific menu helper " + helperClassName)
            val helperClass = Class.forName(fullHelperClassName)
            helperClass.getConstructor(classOf[MenuWiring]).newInstance(wiring) // just instantiate it...
        } catch {
            case cnf: ClassNotFoundException => {
                MenuImpl.LOGGER.warn("No platform-specific menu helper " + helperClassName + " available")
            }
        }
        menuBar.add(fileMenu.getJMenu)
    }

    def getMenuBar: JMenuBar = {
        menuBar
    }
}
