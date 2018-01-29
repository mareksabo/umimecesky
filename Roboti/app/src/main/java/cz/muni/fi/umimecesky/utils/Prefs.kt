package cz.muni.fi.umimecesky.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.pojo.RaceConcept.Companion.initConcepts

/**
 * @author Marek Sabo
 */
private const val IS_IMPORTED = "isImported"
private const val LAST_SEEN_WORD = "lastSeenWord"
private const val LAST_SPINNER_VALUE = "last_spinner_const value"
private const val CHECKED_STATES = "checked_states"
private const val JSON_CONCEPTS = "jsonConcepts"
private const val CURRENT_ROBOT_CONCEPT = "currentRobotConcept"

class Prefs(context: Context) {
    private val gson = Gson()

    private val prefs: SharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    var isImported: Boolean
        get() = prefs.getBoolean(IS_IMPORTED, false)
        set(value) = prefs.edit().putBoolean(IS_IMPORTED, value).apply()

    var lastShownWord: FillWord?
        get() {
            val json = prefs.getString(LAST_SEEN_WORD, null)
            return gson.fromJson<FillWord>(json, FillWord::class.java)
        }
        set(value) {
            prefs.edit().putString(LAST_SEEN_WORD, gson.toJson(value)).apply()
        }

    var seriesAmount: String
        get() = prefs.getString(LAST_SPINNER_VALUE, Constant.INFINITY)
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
            val jsonConcepts = prefs.getString(JSON_CONCEPTS, gson.toJson(initConcepts))
            return gson.fromJson<List<RaceConcept>>(jsonConcepts,
                    object : TypeToken<List<RaceConcept>>() {}.type) as MutableList<RaceConcept>
        }
        set(value) = prefs.edit().putString(JSON_CONCEPTS, gson.toJson(value)).apply()

    var currentRobotConcept: RaceConcept
        get() {
            val json = prefs.getString(CURRENT_ROBOT_CONCEPT, null)
            return gson.fromJson<RaceConcept>(json, RaceConcept::class.java)
        }
        set(value) {
            prefs.edit().putString(CURRENT_ROBOT_CONCEPT, gson.toJson(value)).apply()
        }

}