package cz.muni.fi.umimecesky.game.flappy

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import cz.muni.fi.umimecesky.game.ball.Dimensions
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko.bundleOf

/**
 * @author Marek Sabo
 */
class FlappyLogger(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    enum class Status { CORRECT, WRONG, TOUCH, OUTSIDE }

    // UserId;DeviceDpi;Status;CurrentPassedWords;BestScore;DifficultyName;FPSSpeed;GapSize;IsFirstTimeRun;WordToFill;UniqueRow;
    fun logEvent(status: Status, raceConcept: RaceConcept, currentCount: Int, word: String) {
        firebaseAnalytics.logEvent("flappy_log_everything", bundleOf("flappy_all" to
                "${prefs.userId};" +
                "${Dimensions.deviceDpi()};" +
                "$status;" +
                "$currentCount;" +
                "${prefs.getBestScore(raceConcept)};" +
                "${prefs.flappyGradeName.grade};" +
                "${prefs.flappyFps};" +
                "${prefs.flappyGap};" +
                "${prefs.isFlappyGameIntroduced};" +
                "${word.take(25)};" +
                "${System.currentTimeMillis()};" + // time to make every row unique
                ""
        ))
    }
}
