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

package org.devzendo.morsetrainer

import javax.sound.sampled._
import java.io.File
import org.devzendo.commonapp.gui.{Beautifier, GUIUtils, ThreadCheckingRepaintManager}
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import org.devzendo.commoncode.logging.Logging
import collection.JavaConverters._
import collection.JavaConversions._
import org.devzendo.commonapp.prefs.GuiPrefsStartupHelper
import org.devzendo.commonapp.gui.menu.MenuWiring
import java.awt.{AWTEvent, Toolkit}
import javax.swing.JFrame
import java.awt.event.{WindowEvent, WindowAdapter}
import org.devzendo.morsetrainer.gui._
import org.apache.log4j.BasicConfigurator

object MorseTrainer {
    private val LOGGER = LoggerFactory.getLogger(classOf[MorseTrainer])

    /**
     * @param args the command line arguments.
     */
    def main(args: Array[String]) {
        SLF4JBridgeHandler.install()
        BasicConfigurator.configure()

        val logging = Logging.getInstance()
        val finalArgList = logging.setupLoggingFromArgs(args.toList).asScala.toList

        val javaLibraryPath = System.getProperty("java.library.path")
        LOGGER.debug("java.library.path is '" + javaLibraryPath + "'")
        val quaqua = new File(javaLibraryPath, "libquaqua.jnilib")
        LOGGER.debug("Quaqua JNI library exists there (for Mac OS X)? " + quaqua.exists())

        ThreadCheckingRepaintManager.initialise()

        val applicationContexts = ApplicationContexts.getApplicationContexts

        val springLoader = new SpringLoaderInitialiser(applicationContexts).getSpringLoader

        val prefsStartupHelper: GuiPrefsStartupHelper = springLoader.getBean("guiPrefsStartupHelper", classOf[GuiPrefsStartupHelper])
        prefsStartupHelper.initialisePrefs()

        LOGGER.debug("Application contexts and prefs initialised")

        // Sun changed their recommendations and now recommends the UI be built
        // on the EDT, so I think flagging creation on non-EDT is OK.
        // "We used to say that you could create the GUI on the main thread as
        // long as you didn't modify components that had already been realized.
        // While this worked for most applications, in certain situations it
        // could cause problems."
        // http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
        // So let's create it on the EDT anyway
        //
        GUIUtils.runOnEventThread(new Runnable() {
            def run() {
                try {
                    // Process command line
                    for (i <- 0 until finalArgList.size) {
                        LOGGER.debug("arg " + i + " = " + finalArgList.get(i) + "'")
                    }

                    Beautifier.makeBeautiful()

                    val frameFactory = springLoader.getBean(
                        "morseMainFrameFactory",
                        classOf[MorseMainFrameFactory])
                    val mainFrame = frameFactory.createFrame
                    exitOnClose(mainFrame)

                    val menu = springLoader.getBean("menu", classOf[Menu])
                    menu.initialise()
                    mainFrame.setJMenuBar(menu.getMenuBar)

                    val closeAL = springLoader.getBean(
                        "mainFrameCloseActionListener",
                        classOf[MainFrameCloseActionListener])
                    val menuWiring = springLoader.getBean("menuWiring", classOf[MenuWiring])
                    menuWiring.setActionListener(MorseMenuIdentifiersS.FILE_EXIT, closeAL)

                    val serviceStartup = springLoader.getBean(
                        "serviceStartupAWTEventListener",
                        classOf[ServiceStartupAWTEventListener])
                    Toolkit.getDefaultToolkit.addAWTEventListener(serviceStartup, AWTEvent.WINDOW_EVENT_MASK)

                    mainFrame.setVisible(true)
                } catch {
                    case e: Exception =>
                        LOGGER.error(e.getMessage, e)
                        System.exit(1)
                }
            }

            def exitOnClose(mainFrame: JFrame) {
                mainFrame.addWindowListener(new WindowAdapter() {
                    override def windowClosed(e: WindowEvent ) {
                        LOGGER.debug("Detected window closed")

                        // System.exit(0)
                    }})
            }
        })

        //new MorseTrainer(finalArgList)
    }
}

class MorseTrainer(val argList: List[String]) {


    val clipGen = new ClipGenerator(18, 18, 600)
    play(clipGen.getElementSpace) // to get over initial click

    for (i <- 0 to 8) {
        play(clipGen.getDit)
        play(clipGen.getElementSpace)
        play(clipGen.getDah)
        play(clipGen.getElementSpace)
        play(clipGen.getDit)
        play(clipGen.getCharacterSpace)

    //		System.out.println("data length is " + data.length);
    // due to bug in Java Sound, explicitly exit the VM when
    // the sound has stopped.
    //		clip.addLineListener(new LineListener() {
    //			public void update(LineEvent event) {
    //				if (event.getType() == LineEvent.Type.STOP) {
    //					event.getLine().close();
    //					System.exit(0);
    //				}
    //			}
    //		});

    }

    def play(clip: Clip) {
        clip.start()
        clip.drain()
    }
}
