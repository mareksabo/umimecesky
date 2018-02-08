package cz.muni.fi.umimecesky.logic

import cz.muni.fi.umimecesky.activity.RaceActivity
import cz.muni.fi.umimecesky.pojo.RobotAnimator
import cz.muni.fi.umimecesky.pojo.RobotAnimator.Companion.noWinnerYet
import cz.muni.fi.umimecesky.random
import cz.muni.fi.umimecesky.ui.RaceFinishDialog
import cz.muni.fi.umimecesky.utils.Constant.ROBOT_START_DELAY_MS
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class MoveLogic(private val raceActivity: RaceActivity, animators: Array<RobotAnimator>) {

    private val disposables = ArrayList<Disposable>()

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
        val delay = animator.robot.logic!!.millisecondsPerSolution()

        val disposable = Observable.interval(delay, TimeUnit.MILLISECONDS)
                .delay(random.nextInt(1500) + ROBOT_START_DELAY_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { if (noWinnerYet.get()) animator.processBotMove() }
        disposables.add(disposable)
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
        for (d in disposables) d.dispose()
        raceActivity.disableButtons()
    }

}
