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


import java.awt.event.{ActionEvent, ActionListener}
import org.devzendo.morsetrainer.SessionController
import org.devzendo.commonapp.spring.springloader.SpringLoader

class AbandonTraining(mainPanel: CardLayoutMainPanel,
                      springLoader: SpringLoader) extends ActionListener {

    def actionPerformed(e: ActionEvent) {
        // Getting this is dynamic to resolve construct-time circular dependencies.
        val sessionCtrl = springLoader.getBean("sessionController", classOf[SessionController])
        sessionCtrl.terminate()

        mainPanel.switchToPanel("trainingModesPanel")
    }
}
