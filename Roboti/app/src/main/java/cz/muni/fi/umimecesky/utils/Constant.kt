package cz.muni.fi.umimecesky.utils

import android.graphics.Color
import java.util.*

object Constant {

    const val TRAINING_NEW_WORD_DELAY_MS = 800L
    const val RACE_NEW_WORD_DELAY_MS = 200L
    const val ROBOT_MOVE_ANIMATION_MS = 500L

    const val RAW_HOPS_TO_WIN = 5
    const val STROKE_WIDTH = 8

    const val DEFAULT_COLOR = Color.BLACK
    val CORRECT_COLOR = Color.parseColor("#4C924C")
    const val WRONG_COLOR = Color.RED

    const val FLAPPY_CHOSEN_CATEGORY = "flappy_chosen_category"
    const val TICKED_CATEGORIES = "ticked_categories"
    const val SHARED_PREFS_FILE = "shared_prefs"

    const val INFINITY = "Nekonečno"
    val ROUND_POSSIBILITIES: List<String> = Arrays.asList("10", "20", "35", "50", INFINITY)

    const val ROBOT_START_DELAY_MS = 200L
}
