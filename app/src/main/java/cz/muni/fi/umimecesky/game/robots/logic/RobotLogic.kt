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

interface RobotLogic {

    /**
     * Returns how many milliseconds it takes to solve clue.
     * @return positive number
     */
    fun millisecondsPerSolution(): Long

    /**
     * Checks how often is robot correct.
     * @return number between 0 and 1
     */
    fun correctnessRatio(): Double

    /**
     * How many hops should robot do when correct.
     * @return positive number, usually 1 or 2
     */
    fun hopsPerCorrect(): Int

    companion object {
        fun probabilityTrue(probabilityToReturnTrue: Double): Boolean =
                Math.random() >= 1.0 - probabilityToReturnTrue

        fun roundBy2places(numberToRound: Double): Double =
                Math.round(numberToRound * 100).toDouble() / 100
    }
}
