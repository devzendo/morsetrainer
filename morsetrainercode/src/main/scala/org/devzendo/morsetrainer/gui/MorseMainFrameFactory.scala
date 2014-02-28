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

import org.devzendo.commonapp.gui.{MainFrameFactory, WindowGeometryStore, CursorManager}
import org.devzendo.commonapp.gui.menu.MenuWiring

/**
 * Constructs the MorseMainFrame and connects it with the rest of the app
 * framework.
 *
 * @author matt
 *
 */
class MorseMainFrameFactory(
                                val cursorManager: CursorManager,
                                val windowGeometryStore: WindowGeometryStore,
                                val mainFrameFactory: MainFrameFactory,
                                val menuWiring: MenuWiring,
                                val mainPanel: CardLayoutMainPanel)
{
    def createFrame: MorseMainFrame = {
        val mainFrame = new MorseMainFrame(windowGeometryStore, menuWiring, mainPanel)
        cursorManager.setMainFrame(mainFrame)
        mainFrameFactory.setMainFrame(mainFrame)
        mainFrame
    }
}
