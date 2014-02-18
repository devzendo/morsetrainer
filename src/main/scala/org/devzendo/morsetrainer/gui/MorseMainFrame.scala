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


import java.awt.BorderLayout
import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.{JFrame, WindowConstants}


import org.devzendo.commonapp.gui.menu.MenuWiring
import org.devzendo.commonapp.gui.WindowGeometryStore
import org.slf4j.LoggerFactory

object MorseMainFrame {
    private val LOGGER = LoggerFactory.getLogger(classOf[MorseMainFrame])
    private val MAIN_FRAME_NAME = "main"
}

/**
 * The Morse UI Main Frame, saves its geometry, and handles closing.
 * Construct the main application frame, given the geometry store in which
 * its initial size and location will be loaded, and persisted on close.
 *
 * @author matt
 * @param windowGeometryStore the geometry store
 */
class MorseMainFrame(
                         val windowGeometryStore: WindowGeometryStore,
                         val menuWiring: MenuWiring,
                         val mainPanel: MorseMainPanel) extends JFrame {
    // TODO
    //setIconImage(ResourceLoader.createResourceImageIcon("org/devzendo/morsetrainer/icons/application16x16.gif").getImage())
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)

    setName(MorseMainFrame.MAIN_FRAME_NAME)
    setLayout(new BorderLayout())

    add(mainPanel.panel, BorderLayout.CENTER)

    loadInitialGeometry()
    setupGeometrySaveOnMoveOnClose()

    def loadInitialGeometry() = {
        if (!windowGeometryStore.hasStoredGeometry(this)) {
            pack()
        }
        windowGeometryStore.loadGeometry(this)
    }

    def setupGeometrySaveOnMoveOnClose() = {
        addWindowListener(new WindowAdapter() {
            override def windowClosing(e: WindowEvent) {
                MorseMainFrame.LOGGER.debug("Detected window closing; triggering action listener for FileExit")
                menuWiring.triggerActionListener(MorseMenuIdentifiers.FILE_EXIT)
            }
            override def windowClosed(e: WindowEvent ) {
                MorseMainFrame.LOGGER.debug("Detected window closed")
            }})
    }
}
