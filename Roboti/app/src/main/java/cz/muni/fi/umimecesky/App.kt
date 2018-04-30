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
