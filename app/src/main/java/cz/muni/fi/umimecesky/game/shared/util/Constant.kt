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

package cz.muni.fi.umimecesky.game.shared.util

import android.graphics.Color
import java.util.*

object Constant {

    const val TRAINING_NEW_WORD_DELAY_MS = 800L
    const val RACE_NEW_WORD_DELAY_MS = 200L
    const val ROBOT_MOVE_ANIMATION_MS = 500L

    const val RAW_HOPS_TO_WIN = 5
    const val STROKE_WIDTH = 8

    const val UNSET_ID = -1L

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
