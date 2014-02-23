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

package org.devzendo.morsetrainer.gui.dialogs

import javax.swing.{JTabbedPane, JComponent, BorderFactory, JPanel}
import java.awt.Color

trait DialogTools {
    def padded(gap: Int, comp: JComponent): JPanel = {
        val panel = new JPanel()
        panel.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap))
        panel.add(comp)
        panel
    }

    def padded(comp: JComponent): JPanel = padded(10, comp)
}