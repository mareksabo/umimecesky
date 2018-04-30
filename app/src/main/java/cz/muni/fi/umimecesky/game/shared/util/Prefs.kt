package cz.muni.fi.umimecesky.game.shared.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.muni.fi.umimecesky.enums.Difficulty
import cz.muni.fi.umimecesky.enums.Gap
import cz.muni.fi.umimecesky.enums.toDifficulty
import cz.muni.fi.umimecesky.enums.toGap
import cz.muni.fi.umimecesky.game.ball.Dimensions.isTablet
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.model.FillWord.Companion.EMPTY_WORD
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept.Companion.initConcepts

/**
 * @author Marek Sabo
 */
private const val LAST_SPINNER_VALUE = "lastSpinnerValue"
private const val CHECKED_STATES = "checkedStates"

private const val RACE_CONCEPTS = "raceConcepts"
private const val CURRENT_ROBOT_CONCEPT = "currentRobotConcept"

private const val LAST_RANDOM_WORD = "lastRandomWord"
private const val HOLES_AMOUNT = "holesAmount"
private const val ROTATION_MODE = "rotationMode"
private const val HOLE_WORD_GRADE = "holeWordGrade"
private const val BALL_WEIGHT = "ballWeight"
private const val WAS_INTRODUCED = "wasIntroduced"
private const val FIRST_HOLE_RUN = "firstHoleRun"
private const val USER_ID = "userId"

private const val BEST_USER_SCORE = "bestUserScore"
private const val FLAPPY_GRADE_NAME = "flappyGradeName"
private const val FLAPPY_INTRODUCE = "flappyIntroduce"
private const val FLAPPY_FPS = "flappyFPS"
private const val FLAPPY_GAP = "flappyGap"


class Prefs(context: Context) {
    private val gson = Gson()

    private val prefs =
            context.getSharedPreferences(Constant.SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    /// TRAINING ///

    var seriesAmount: String
        get() = prefs.getString(LAST_SPINNER_VALUE, Constant.ROUND_POSSIBILITIES[0])
        set(value) = prefs.edit().putString(LAST_SPINNER_VALUE, value).apply()

    var checkedStates: BooleanArray
        get() = prefs.getString(CHECKED_STATES, "")
                .split(",")
                .map { it.toBoolean() }
                .toBooleanArray()
        set(value) = prefs.edit().putString(CHECKED_STATES, value.joinToString(",")).apply()

    /// ROBOT GAME ///

    /**
     * Gets categories with saved player's level state.
     */
    var maxRobotsCategories: MutableList<RaceConcept>
        get() {
            val jsonConcepts = prefs.getString(RACE_CONCEPTS, gson.toJson(initConcepts))
            return gson.fromJson<List<RaceConcept>>(jsonConcepts,
                    object : TypeToken<List<RaceConcept>>() {}.type) as MutableList<RaceConcept>
        }
        set(value) = prefs.edit().putString(RACE_CONCEPTS, gson.toJson(value)).apply()

    var currentRobotConcept: RaceConcept
        get() {
            val json = prefs.getString(CURRENT_ROBOT_CONCEPT, null)
            return gson.fromJson<RaceConcept>(json, RaceConcept::class.java)
        }
        set(value) = prefs.edit().putString(CURRENT_ROBOT_CONCEPT, gson.toJson(value)).apply()

    /// HOLE GAME ///

    var lastRandomWord: FillWord
        get() {
            val json = prefs.getString(LAST_RANDOM_WORD, null) ?: return EMPTY_WORD
            return gson.fromJson<FillWord>(json, FillWord::class.java)
        }
        set(value) = prefs.edit().putString(LAST_RANDOM_WORD, gson.toJson(value)).apply()

    var holesAmount: Int
        get() = prefs.getInt(HOLES_AMOUNT, 10)
        set(value) = prefs.edit().putInt(HOLES_AMOUNT, value).apply()

    var rotationMode: Int
        get() = prefs.getInt(ROTATION_MODE, 1)
        set(value) = prefs.edit().putInt(ROTATION_MODE, value).apply()

    var holeWordGrade: Int
        get() = prefs.getInt(HOLE_WORD_GRADE, 1)
        set(value) = if (value >= 0) prefs.edit().putInt(HOLE_WORD_GRADE, value).apply() else {
        }

    var ballWeight: Int
        get() = prefs.getInt(BALL_WEIGHT, 2 + if (isTablet()) 1 else 0)
        set(value) = prefs.edit().putInt(BALL_WEIGHT, value).apply()

    var wasBallGameIntroduced: Boolean
        get() = prefs.getBoolean(WAS_INTRODUCED, false)
        set(value) = prefs.edit().putBoolean(WAS_INTRODUCED, value).apply()

    var isFirstTimeRun: Boolean
        get() = prefs.getBoolean(FIRST_HOLE_RUN, true)
        set(value) = prefs.edit().putBoolean(FIRST_HOLE_RUN, value).apply()

    var userId: Long
        get() = prefs.getLong(USER_ID, System.currentTimeMillis()) // todo: -1 when everybody has ID
        set(value) = prefs.edit().putLong(USER_ID, value).apply()

    /// FLAPPY BIRD GAME ///

    fun getBestScore(raceConcept: RaceConcept): Int =
            bestUserCountArray()[RaceConcept.conceptNames.indexOf(raceConcept.name)]

    fun setBestScore(raceConcept: RaceConcept, count: Int) {
        val bestUserCountArray = bestUserCountArray()
        bestUserCountArray[RaceConcept.conceptNames.indexOf(raceConcept.name)] = count
        prefs.edit().putString(BEST_USER_SCORE, bestUserCountArray.joinToString(",")).apply()
    }

    private fun bestUserCountArray(): IntArray {
        return prefs.getString(BEST_USER_SCORE,
                IntArray(RaceConcept.conceptNames.size).joinToString(","))
                .split(",")
                .map { it.toInt() }
                .toIntArray()
    }

    var isFlappyGameIntroduced: Boolean
        get() = prefs.getBoolean(FLAPPY_INTRODUCE, false)
        set(value) = prefs.edit().putBoolean(FLAPPY_INTRODUCE, value).apply()

    var flappyGradeName: Difficulty
        get() = prefs.getString(FLAPPY_GRADE_NAME, Difficulty.Medium.name).toDifficulty()
        set(value) = prefs.edit().putString(FLAPPY_GRADE_NAME, value.name).apply()

    var flappyGap: Gap
        get() = prefs.getInt(FLAPPY_GAP, Gap.Big.size).toGap()
        set(value) = prefs.edit().putInt(FLAPPY_GAP, value.size).apply()

    var flappyFps: Int
        get() = prefs.getInt(FLAPPY_FPS, 35)
        set(value) = prefs.edit().putInt(FLAPPY_FPS, value).apply()

}