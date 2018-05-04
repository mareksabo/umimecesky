/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky

import android.app.Application
import android.os.StrictMode
import cz.muni.fi.umimecesky.game.shared.util.Prefs
import java.security.SecureRandom


/**
 * @author Marek Sabo
 */
val prefs: Prefs by lazy {
    App.prefs!!
}

val random = SecureRandom()

class App : Application() {
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)

        if (BuildConfig.DEBUG) {
            turnOnStrictMode()
        }
    }

    private fun turnOnStrictMode() {
        val tpb = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyFlashScreen()
        StrictMode.setThreadPolicy(tpb.build())

        StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .detectLeakedClosableObjects()
    }
}
