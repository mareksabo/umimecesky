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

package cz.muni.fi.umimecesky.game.robots.logic

import cz.muni.fi.umimecesky.game.robots.logic.RobotLogic.Companion.roundBy2places
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept

class BotLogicSlow(private val concept: RaceConcept) : RobotLogic {

    override fun millisecondsPerSolution(): Long = 3500L - (1500L * concept.levelProgress()).toLong()

    override fun correctnessRatio(): Double {
        val addition = if (isBeforeHalf)
            roundBy2places(0.4 * concept.levelProgress())
        else
            0.1 * concept.levelProgress()
        return 0.75 + addition
    }

    override fun hopsPerCorrect(): Int = if (isBeforeHalf) 1 else 2

    private val isBeforeHalf: Boolean
        get() = concept.levelProgress() < 0.5
}
