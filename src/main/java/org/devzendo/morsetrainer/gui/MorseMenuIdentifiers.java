package org.devzendo.morsetrainer.gui;

/**
 * Copyright (C) 2008-2014 Matt Gumbley, DevZendo.org <http://devzendo.org>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.devzendo.commonapp.gui.menu.MenuIdentifier;

/**
 * Menu identifiers.
 *
 * <p>
 * Defined in Java since I can't figure out yet how to declare them in Scala
 * without them being visible to Java as constants, not functions.
 *
 * @author matt
 *
 */
public final class MorseMenuIdentifiers {
    /**
     *
     */
    public static final MenuIdentifier FILE_EXIT = new MenuIdentifier(
            "FileExit");

    /**
     *
     */
    public static final MenuIdentifier TOOLS_OPTIONS = new MenuIdentifier(
            "ToolsOptions");

    /**
     *
     */
    public static final MenuIdentifier HELP_ABOUT = new MenuIdentifier(
            "HelpAbout");
}
