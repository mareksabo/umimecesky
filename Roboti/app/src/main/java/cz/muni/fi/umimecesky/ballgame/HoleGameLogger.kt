package cz.muni.fi.umimecesky.ballgame

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko.bundleOf

/**
 * @author Marek Sabo
 */
class HoleGameLogger(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    private var startingTime: Long? = null
    private var touchedWrongAnswer = false
    private var holesFallAmount = 0

    private var wordToFill: FillWord? = null

    fun touchedWrongAnswer() {
        touchedWrongAnswer = true
    }

    fun incrementHolesFallAmount() {
        holesFallAmount++
    }

    fun userPropertiesSetup() {
        firebaseAnalytics.setUserProperty("has_tablet", "${Dimensions.isTablet()}")
        firebaseAnalytics.setUserProperty("device_dpi", "${Dimensions.deviceDpi()}")
    }

    fun logGameSettings() {
        val bundle = bundleOf(
                "difficulty_type" to HoleGameActivity.difficultyTypes[prefs.holeWordGrade],
                "holes_amount" to prefs.holesAmount
        )
        firebaseAnalytics.logEvent("difficulty_with_quantity", bundle)
    }

    fun startNewWord(currentWord: FillWord) {
        startingTime = System.nanoTime()
        this.wordToFill = currentWord
    }

    // UserId;DeviceDpi;ElapsedSeconds;WordDifficultyGrade;HolesAmount;HolesFallAmount;IsWrongHoleTouched;IsFirstTimeRun;RotationType;BallWeightType;FinishedTime;ScreenInches;WordToFill;
    fun logFinished() {
        firebaseAnalytics.logEvent("log_everything", bundleOf(
                "all_in_one" to generateLogData()
        ))
    }

    fun logUnfinished() {
        firebaseAnalytics.logEvent("unfinished_hole", bundleOf(
                "all_unfinished" to generateLogData()
        ))
    }

    private fun generateLogData(): String {
        return startingTime?.run {
            val elapsedSeconds = (System.nanoTime() - this) / 1_000_000_000
            "${prefs.userId};" +
                    "${Dimensions.deviceDpi()};" +
                    "$elapsedSeconds;" +
                    "${HoleGameActivity.difficultyTypes[prefs.holeWordGrade]};" +
                    "${prefs.holesAmount};" +
                    "$holesFallAmount;" +
                    "$touchedWrongAnswer;" +
                    "${prefs.isFirstTimeRun};" +
                    "${prefs.rotationMode};" +
                    "${prefs.ballWeight};" +
                    "${System.currentTimeMillis()};" + // time to make every row unique
                    "${Dimensions.diagonalInches()};" +
                    "${wordToFill?.wordFilled};" +
                    ""
        } ?: ""
    }
}
