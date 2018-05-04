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

import cz.muni.fi.umimecesky.game.robots.RaceActivity
import cz.muni.fi.umimecesky.game.robots.RaceFinishDialog
import cz.muni.fi.umimecesky.game.robots.RobotAnimator
import cz.muni.fi.umimecesky.game.robots.RobotAnimator.Companion.noWinnerYet
import cz.muni.fi.umimecesky.game.shared.util.Constant.ROBOT_START_DELAY_MS
import cz.muni.fi.umimecesky.random
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
