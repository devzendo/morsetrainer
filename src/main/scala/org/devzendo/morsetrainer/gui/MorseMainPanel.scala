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


import scala.collection.mutable.ListBuffer

import java.awt.{Dimension, CardLayout}
import javax.swing.{SwingUtilities, JPanel}

import org.devzendo.commonapp.gui.GUIUtils

object MorseMainPanel {
    val BlankPanelName = "*blank*panel*"
}
class MorseMainPanel {
    private val cardLayout = new CardLayout()
    val panel = new JPanel(cardLayout)
    val lookup = scala.collection.mutable.Map.empty[String, JPanel]
    val panelNames = ListBuffer.empty[String]
    panel.setPreferredSize(new Dimension(640, 480))
    panel.add(new JPanel(), MorseMainPanel.BlankPanelName)
    switchToBlank()

    def mainPanel = panel
    private var currentPanelName = MorseMainPanel.BlankPanelName

    def currPanelName = {
        assert(SwingUtilities.isEventDispatchThread, "currPanelName not invoked on EDT")
        currentPanelName
    }

    def addPanel(panelName: String, newPanel: JPanel) = {
        GUIUtils.runOnEventThread(new Runnable() {
            def run() = {
                lookup += (panelName -> newPanel)
                panelNames += panelName
                panel.add(newPanel, panelName)
            }
        })
    }

    def switchToPanel(panelName: String) = {
        GUIUtils.runOnEventThread(new Runnable() {
            def run() = {
                lookup.get(panelName) match {
                    case Some(p) =>
                        cardLayout.show(panel, panelName)
                        currentPanelName = panelName
                    case None => // do, nothing, it wasn't added
                }
            }
        })
    }

    private def switchToBlank() = {
        currentPanelName = MorseMainPanel.BlankPanelName
        cardLayout.show(panel, MorseMainPanel.BlankPanelName)
    }

    private def switchToIndex(index: Int) = {
        currentPanelName = panelNames(index)
        cardLayout.show(panel, currentPanelName)
    }

    def removePanel(panelName: String) = {
        GUIUtils.runOnEventThread(new Runnable() {
            def run() = {
                lookup.get(panelName) match {
                    case Some(p) =>
                        val currIndexBeforeRemove = panelNames indexOf panelName
                        panel.remove(p)
                        lookup -= panelName
                        panelNames -= panelName
                        if (panelNames.length == 0) {
                            switchToBlank()
                        } else {
                            switchToIndex(0.max(currIndexBeforeRemove - 1))
                        }
                    case None => // do, nothing, it wasn't added
                }
            }
        })
    }
}
