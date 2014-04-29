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

import javax.swing.JFrame
import org.devzendo.morsetrainer.prefs.{MorseTrainerPrefs, DefaultPrefsInstantiator}
import org.devzendo.commonapp.prefs.DefaultPrefsLocation
import org.devzendo.commonapp.gui.WindowGeometryStore
import java.awt.BorderLayout

object ReportPanelDriver {
    val reportPanelName: String = "reportPanel"

    def main(args: Array[String]) {
        val frame = new JFrame()
        frame.setName(MorseMainFrame.MAIN_FRAME_NAME)
        frame.setLayout(new BorderLayout())

        val prefsLoc = new DefaultPrefsLocation(".morsetrainer", "morsetrainer.prefs")
        val prefsInst = new DefaultPrefsInstantiator()
        val prefs = prefsInst.instantiatePrefs(prefsLoc).asInstanceOf[MorseTrainerPrefs]
        val geomStore = new MorseTrainerWindowGeometryStorePersistence(prefs)
        val wgs = new WindowGeometryStore(geomStore)
        wgs.loadGeometry(frame)

        val card = new CardLayoutMainPanel()
        frame.add(card.mainPanel, BorderLayout.CENTER)

        val report = new ReportPanel(prefs, card)
        card.addPanel("reportPanel", report)

        card.switchToPanel(reportPanelName)

        frame.setVisible(true)
    }

}
