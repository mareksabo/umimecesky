package cz.muni.fi.umimecesky.pojo

import android.view.ViewPropertyAnimator
import android.widget.ImageView
import cz.muni.fi.umimecesky.activity.RaceActivity.Companion.robotMovePx
import cz.muni.fi.umimecesky.activity.totalMovesToWin
import cz.muni.fi.umimecesky.utils.Constant.ROBOT_MOVE_ANIMATION_MS
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Marek Sabo
 */
class RobotAnimator(view: ImageView, val robot: Robot) {
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
        animator.translationXBy(-robotMovePx!!)
        remainingHopsToWin++
    }

    private fun animateForwardWithAction(runnableBefore: Runnable?, runnableAfter: Runnable?) {
        animator.translationXBy(robotMovePx!! * limitHopsWhenWon())
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
