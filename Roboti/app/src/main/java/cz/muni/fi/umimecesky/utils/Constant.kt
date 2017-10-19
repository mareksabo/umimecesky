package cz.muni.fi.umimecesky.utils

import android.graphics.Color
import java.util.*

object Constant {

    val TRAINING_NEW_WORD_DELAY_MS = 800
    val RACE_NEW_WORD_DELAY_MS = 200
    val ANIMATION_DURATION = 320

    val RAW_HOPS_TO_WIN = 5
    val STROKE_WIDTH = 8

    val DEFAULT_COLOR = Color.BLACK
    val CORRECT_COLOR = Color.parseColor("#4C924C")
    val WRONG_COLOR = Color.RED

    val LAST_FILLED_WORD = "lastWord"
    val IS_FILLED = "isFilled"
    val TICKED_CATEGORIES_EXTRA = "ticked_categories"
    val RACE_CONCEPT_EXTRA = "concept"
    val LAST_SPINNER_VALUE = "last_spinner_value"
    val SHARED_PREFS_FILE = "shared_prefs"
    val CHECKED_STATES = "checked_states"

    val INFINITY = "Nekonečno"
    val ROUND_POSSIBILITIES: MutableList<String> = Arrays.asList("10", "20", "35", "50", INFINITY)

    val ROBOT_START_DELAY_MS = 200
}
