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

import android.view.ViewPropertyAnimator
import android.widget.ImageView
import cz.muni.fi.umimecesky.game.shared.util.Constant.ROBOT_MOVE_ANIMATION_MS
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author Marek Sabo
 */
class RobotAnimator(view: ImageView, val robot: Robot, private val totalMovesToWin: Int,
                    private val robotMovePx: Float) {
    var runnableBefore: Runnable? = null
    var runnableAfter: Runnable? = null
    val isAI = robot.isAI

    private var remainingHopsToWin = totalMovesToWin
    private val animator: ViewPropertyAnimator = view.animate()


    private val isOneStepToWin: Boolean
        get() = remainingHopsToWin in 1..robot.hopsPerCorrect

    private val isNotAtStart: Boolean
        get() = remainingHopsToWin != totalMovesToWin

    init {
        animator.duration = ROBOT_MOVE_ANIMATION_MS
    }

    fun processBotMove() {
        if (!isAI) return
        if (robot.canMove()) applyCorrect() else applyWrong()
    }

    fun applyCorrect() {
        val previousWasCorrect = robot.lastWasCorrect
        robot.lastWasCorrect = true

        if (previousWasCorrect && noWinnerYet.get()) decideAnimationForward()
    }

    fun applyWrong() {
        if (robot.lastWasCorrect) {
            robot.lastWasCorrect = false
            if (isNotAtStart) animateBackward()
        }
    }

    private fun decideAnimationForward() {
        if (isOneStepToWin) {
            animateForwardWithAction(runnableBefore, runnableAfter)
        } else {
            animateForwardWithAction(null, null)
        }
        remainingHopsToWin -= limitHopsWhenWon()
    }

    private fun animateBackward() {
        animator.translationXBy(-robotMovePx)
        remainingHopsToWin++
    }

    private fun animateForwardWithAction(runnableBefore: Runnable?, runnableAfter: Runnable?) {
        animator.translationXBy(robotMovePx * limitHopsWhenWon())
                .withStartAction(runnableBefore)
                .withEndAction(runnableAfter)
    }

    private fun limitHopsWhenWon(): Int {
        val hopsBefore = robot.hopsPerCorrect
        return if (remainingHopsToWin >= hopsBefore) hopsBefore else remainingHopsToWin
    }

    override fun toString() = "RobotAnimator(robot=$robot, remainingHopsToWin=$remainingHopsToWin"

    companion object {
        var noWinnerYet = AtomicBoolean(true)
    }

}
