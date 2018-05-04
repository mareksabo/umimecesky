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

package cz.muni.fi.umimecesky.game.robots

import cz.muni.fi.umimecesky.game.robots.logic.RobotLogic

/**
 * @author Marek Sabo
 */
data class Robot(val logic: RobotLogic?) {

    constructor() : this(null)

    @Volatile var lastWasCorrect = true

    val hopsPerCorrect: Int
        get() = logic?.hopsPerCorrect() ?: 1

    val isAI: Boolean
        get() = logic != null

    fun canMove() = if (isAI) RobotLogic.probabilityTrue(logic!!.correctnessRatio()) else false

}