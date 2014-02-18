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

import org.devzendo.commonapp.gui.{SwingWorker, CursorManager, WindowGeometryStore}
import javax.swing.JFrame
import java.awt.event.{ActionEvent, ActionListener}
import org.devzendo.commonapp.service.ServiceManager
import org.slf4j.LoggerFactory


object MainFrameCloseActionListener {
    private val LOGGER = LoggerFactory.getLogger(classOf[MainFrameCloseActionListener])
}

class MainFrameCloseActionListener(
                                      val windowGeometryStore: WindowGeometryStore,
                                      val mainFrame: JFrame,
                                      val cursorManager: CursorManager,
                                      val serviceManager: ServiceManager) extends ActionListener {

    def actionPerformed(e: ActionEvent) {
        cursorManager.hourglass(this.getClass.getSimpleName)

        val worker = new SwingWorker() {
            override def construct(): Object = {
                Thread.currentThread().setName("Service Shutdown")
                MainFrameCloseActionListener.LOGGER.info("Service manager shutdown...")
                serviceManager.shutdown()
                MainFrameCloseActionListener.LOGGER.info("...end of service manager shutdown")
                null
            }

            override def finished() = {
                MainFrameCloseActionListener.LOGGER.info("Saving main frame geometry")
                windowGeometryStore.saveGeometry(mainFrame)
                cursorManager.normal(this.getClass.getSimpleName)
                MainFrameCloseActionListener.LOGGER.info("Disposing main frame")
                mainFrame.dispose()
            }
        }
        worker.start()
    }
}
