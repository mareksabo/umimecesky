package cz.muni.fi.umimecesky.utils

import android.graphics.Color
import java.util.*

object Constant {

    const val TRAINING_NEW_WORD_DELAY_MS = 800
    const val RACE_NEW_WORD_DELAY_MS = 200
    const val ANIMATION_DURATION = 320

    const val RAW_HOPS_TO_WIN = 5
    const val STROKE_WIDTH = 8

    const val DEFAULT_COLOR = Color.BLACK
    val CORRECT_COLOR = Color.parseColor("#4C924C")
    const val WRONG_COLOR = Color.RED

    const val TICKED_CATEGORIES_EXTRA = "ticked_categories"
    const val RACE_CONCEPT_EXTRA = "concept"
    const val SHARED_PREFS_FILE = "shared_prefs"

    const val INFINITY = "Nekonečno"
    val ROUND_POSSIBILITIES: List<String> = Arrays.asList("10", "20", "35", "50", INFINITY)

    const val ROBOT_START_DELAY_MS = 200
}
