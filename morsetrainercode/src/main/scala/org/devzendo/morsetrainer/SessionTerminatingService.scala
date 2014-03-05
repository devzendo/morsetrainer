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

import org.devzendo.commonapp.service.{ServiceManagerProxy, Service}
import org.devzendo.commonapp.spring.springloader.SpringLoader

class SessionTerminatingService(springLoader: SpringLoader) extends Service {
    def startup(proxy: ServiceManagerProxy) {

    }

    def prepareForShutdown() {
        // have to get the sessionController dynamically; if we inject it into
        // the ctor, its dependencies start all sorts of graphical stuff up on
        // the wrong thread
        val sessionController = springLoader.getBean("sessionController", classOf[SessionController])
        sessionController.terminate
    }

    def shutdown() {

    }
}
