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
class FlappyLogger(context: Context, private val isGameIntroduced: Boolean) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    enum class Status { CORRECT, WRONG, TOUCH, OUTSIDE }

    // UserId;DeviceDpi;Status;CurrentPassedWords;BestScore;DifficultyName;FPSSpeed;GapSize;WasIntroduced;WordToFill;UniqueRow;
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
                "$isGameIntroduced;" +
                "${word.take(25)};" +
                "${System.currentTimeMillis()};" + // time to make every row unique
                ""
        ))
    }
}
