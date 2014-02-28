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

import org.devzendo.commonapp.spring.springloader.SpringLoader
import org.devzendo.commonapp.service.{ServiceManagerProxy, Service}
import org.devzendo.commonapp.gui.GUIUtils
import org.slf4j.LoggerFactory
import javax.swing.JPanel

object MainFramePopulatingService {
    private val LOGGER = LoggerFactory.getLogger(classOf[MainFramePopulatingService])

}
class MainFramePopulatingService(springLoader: SpringLoader, mainPanel: MorseMainPanel) extends Service {
    import MainFramePopulatingService._

    def addToMainPanel(panelBeanName: String) {
        GUIUtils.runOnEventThread(new Runnable() {
            def run() {
                try {
                    val jPanel = springLoader.getBean(panelBeanName, classOf[JPanel])
                    mainPanel.addPanel(panelBeanName, jPanel)
                } catch {
                    case e: Exception =>
                        LOGGER.error(e.getMessage, e)
                }
            }
        })
    }

    def startup(proxy: ServiceManagerProxy) {
        addToMainPanel("trainingModesPanel")

        GUIUtils.runOnEventThread(new Runnable() {
            def run() {
                try {
                    mainPanel.switchToPanel("trainingModesPanel")
                } catch {
                    case e: Exception =>
                        LOGGER.error(e.getMessage, e)
                }
            }
        })
    }

    def prepareForShutdown() {}

    def shutdown() {}
}
