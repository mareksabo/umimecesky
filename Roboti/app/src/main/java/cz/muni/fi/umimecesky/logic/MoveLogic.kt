package cz.muni.fi.umimecesky.logic

import android.os.Handler
import cz.muni.fi.umimecesky.activity.RaceActivity
import cz.muni.fi.umimecesky.pojo.RobotAnimator
import cz.muni.fi.umimecesky.pojo.RobotAnimator.Companion.noWinnerYet
import cz.muni.fi.umimecesky.ui.RaceFinishDialog
import cz.muni.fi.umimecesky.utils.Constant
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MoveLogic(private val raceActivity: RaceActivity, animators: Array<RobotAnimator>) {

    private val handlers = ArrayList<Handler>()

    private val finishDialog: RaceFinishDialog = RaceFinishDialog(raceActivity)

    init {
        noWinnerYet = AtomicBoolean(true)

        for (animator in animators) {
            if (animator.isAI) {
                setupBot(animator)
                animator.runnableAfter = actionWhenUserLoses()
            } else {
                animator.runnableBefore = actionWhenUserWins()
            }
        }
    }

    private fun setupBot(animator: RobotAnimator) {
        val logic = animator.robot.logic!!
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                animator.processBotMove()
                if (noWinnerYet.get()) {
                    handler.postDelayed(this, logic.millisecondsPerSolution().toLong())
                }
            }
        }, (Math.random() * 2000 + Constant.ROBOT_START_DELAY_MS).toLong())
        handlers.add(handler)
    }

    private fun actionWhenUserWins(): Runnable = Runnable {
        if (noWinnerYet.getAndSet(false)) {
            stopBotsAndInput()
            finishDialog.showWinningDialog()
        }
    }

    private fun actionWhenUserLoses(): Runnable = Runnable {
        if (noWinnerYet.getAndSet(false)) {
            stopBotsAndInput()
            finishDialog.showLosingDialog()
        }
    }

    fun stopBotsAndInput() {
        for (handler in handlers) {
            handler.removeCallbacksAndMessages(null)
        }
        raceActivity.disableButtons()
    }

}
