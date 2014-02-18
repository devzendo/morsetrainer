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

import org.apache.log4j.Logger
import javax.swing.JFrame
import org.devzendo.commonapp.gui.{SwingWorker, CursorManager}
import java.awt.event.{WindowEvent, AWTEventListener}
import java.awt.AWTEvent
import org.devzendo.commonapp.service.ServiceManager

object ServiceStartupAWTEventListener {
    private val LOGGER = Logger.getLogger(classOf[ServiceStartupAWTEventListener])
}

/**
 * The ServiceStartupAWTEventListener is attached as a listener
 * to the main JFrame, and listens for it becoming visible. At
 * this point, it triggers the Service startup on a separate
 * thread, surrounding this with an hourglass cursor.
 *
 * @author matt
 *
 */
class ServiceStartupAWTEventListener(
                                        val mainFrame: JFrame,
                                        val cursorManager: CursorManager,
                                        val serviceManager : ServiceManager) extends AWTEventListener {

    def eventDispatched(event: AWTEvent) = {
        ServiceStartupAWTEventListener.LOGGER.debug("Event received")
        if (event.getID == WindowEvent.WINDOW_OPENED && event.getSource.equals(mainFrame)) {
            startLifecycle()
        }
    }

    private def startLifecycle() = {
        cursorManager.hourglass(this.getClass.getSimpleName)
        val worker = new SwingWorker() {
            override def construct(): Object = {
                Thread.currentThread().setName("Service Startup")
                ServiceStartupAWTEventListener.LOGGER.info("Service manager startup...")
                serviceManager.startup()
                ServiceStartupAWTEventListener.LOGGER.info("...end of service manager startup")
                null
            }

            override def finished() = {
                cursorManager.normal(this.getClass.getSimpleName)
            }
        }
        worker.start()
    }
}
