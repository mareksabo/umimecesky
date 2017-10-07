package cz.muni.fi.umimecesky.pojo

import android.view.ViewPropertyAnimator
import android.widget.ImageView
import cz.muni.fi.umimecesky.utils.Constant
import cz.muni.fi.umimecesky.utils.Global
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractRobot(view: ImageView, val hopsPerCorrect: Int) {

    private var runnableBefore: Runnable? = null
    private var runnableAfter: Runnable? = null

    @Volatile protected var lastWasCorrect = true
    private var remainingHopsToWin: Int = 0
    private val animator: ViewPropertyAnimator = view.animate()

    private val ROBOT_MOVE: Float
    private val TOTAL_MOVES_TO_WIN: Int

    abstract fun applyCorrect()

    abstract fun applyWrong()

    init {
        animator.duration = Constant.ANIMATION_DURATION.toLong()

        val calculateDp = Global.calculateDp
        ROBOT_MOVE = calculateDp.calculateRobotMovePx()

        TOTAL_MOVES_TO_WIN = calculateDp.winMovesCount
        remainingHopsToWin = TOTAL_MOVES_TO_WIN
    }

    protected fun moveForward() {
        lastWasCorrect = true
        if (isOneStepToWin) {
            decideAnimationForward()
        } else if (noWinnerYet!!.get()) {
            decideAnimationForward()
        }
    }

    private fun decideAnimationForward() {
        if (isOneStepToWin) {
            animateForwardWithAction(runnableBefore, runnableAfter)
        } else {
            animateForward()
        }
        remainingHopsToWin -= limitedHopsPerCorrect()
    }

    protected fun moveBackward() {
        if (lastWasCorrect) {
            lastWasCorrect = false

            if (isNotAtStart) {
                animateBackward()
            }
        }
    }

    private fun animateBackward() {
        animator.translationXBy(-ROBOT_MOVE)
        remainingHopsToWin++
    }

    private fun animateForward() {
        animateForwardWithAction(null, null)
    }

    protected fun animateForwardWithAction(runnableBefore: Runnable?, runnableAfter: Runnable?) {
        animator.translationXBy(ROBOT_MOVE * limitedHopsPerCorrect())
                .withStartAction(runnableBefore)
                .withEndAction(runnableAfter)
    }

    private fun limitedHopsPerCorrect(): Int {
        return limitHopsWhenWon(hopsPerCorrect)
    }

    private fun limitHopsWhenWon(hopsBefore: Int): Int {
        if (remainingHopsToWin >= hopsBefore) {
            return hopsBefore
        } else {
            return remainingHopsToWin
        }

    }

    private val isOneStepToWin: Boolean
        get() = 0 < remainingHopsToWin && remainingHopsToWin <= hopsPerCorrect

    private val isNotAtStart: Boolean
        get() = remainingHopsToWin != TOTAL_MOVES_TO_WIN

    fun setRunnableAfter(runnableAfter: Runnable) {
        this.runnableAfter = runnableAfter
    }

    fun setRunnableBefore(runnableBefore: Runnable) {
        this.runnableBefore = runnableBefore
    }

    companion object {
        var noWinnerYet = AtomicBoolean(true)
    }
}
