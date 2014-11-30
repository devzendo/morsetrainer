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

import javax.swing.JMenu
import javax.swing.JSeparator


import org.devzendo.commonapp.gui.menu.AbstractRebuildableMenuGroup
import org.devzendo.commonapp.gui.menu.MenuWiring

import org.devzendo.commoncode.os.OSTypeDetect

import org.slf4j.LoggerFactory

object FileMenu {
    private val LOGGER = LoggerFactory.getLogger(classOf[FileMenu])
}

class FileMenu(val menuWiring: MenuWiring) extends AbstractRebuildableMenuGroup(menuWiring) {
    private val fileMenu: JMenu = new JMenu("File")
    private val osType = OSTypeDetect.getInstance().getOSType

    fileMenu.setMnemonic('F')
    // Trigger the first build; initially we'll have no recent files list.
    // Need to do an initial rebuild so the menu wiring is initially populated
    rebuildMenuGroup()

    def getJMenu: JMenu = {
        fileMenu
    }

    def rebuildMenuGroup() = {
        fileMenu.removeAll()

        if (osType != OSTypeDetect.OSType.MacOSX) {
            fileMenu.add(new JSeparator())

            createMenuItem(MorseMenuIdentifiers.FILE_EXIT, "Exit", 'x', fileMenu)
        }
    }
}
