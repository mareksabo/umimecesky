package cz.muni.fi.umimecesky.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.muni.fi.umimecesky.labyrinth.SimulationView.Companion.EMPTY_WORD
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.pojo.RaceConcept.Companion.initConcepts

/**
 * @author Marek Sabo
 */
private const val IS_IMPORTED = "isImported"
private const val LAST_SPINNER_VALUE = "lastSpinnerValue"
private const val CHECKED_STATES = "checkedStates"
private const val RACE_CONCEPTS = "raceConcepts"
private const val CURRENT_ROBOT_CONCEPT = "currentRobotConcept"
private const val LAST_RANDOM_WORD = "lastRandomWord"
private const val HOLES_AMOUNT = "holesAmount"
private const val ROTATION_MODE = "rotationMode"
private const val HOLE_WORD_GRADE = "holeWordGrade"
private const val BALL_WEIGHT = "ballWeight"

class Prefs(context: Context) {
    private val gson = Gson()

    private val prefs =
            context.getSharedPreferences(Constant.SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    var isImported: Boolean
        get() = prefs.getBoolean(IS_IMPORTED, false)
        set(value) = prefs.edit().putBoolean(IS_IMPORTED, value).apply()

    var seriesAmount: String
        get() = prefs.getString(LAST_SPINNER_VALUE, Constant.ROUND_POSSIBILITIES[0])
        set(value) = prefs.edit().putString(LAST_SPINNER_VALUE, value).apply()

    var checkedStates: BooleanArray
        get() = prefs.getString(CHECKED_STATES, "")
                .split(",")
                .map { it.toBoolean() }
                .toBooleanArray()
        set(value) = prefs.edit().putString(CHECKED_STATES, value.joinToString(",")).apply()

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
        set(value) = prefs.edit().putInt(HOLE_WORD_GRADE, value).apply()

    var ballWeight: Int
        get() = prefs.getInt(BALL_WEIGHT, 2)
        set(value) = prefs.edit().putInt(BALL_WEIGHT, value).apply()

}