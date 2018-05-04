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

package cz.muni.fi.umimecesky.game.practise

import android.animation.ObjectAnimator
import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.DecelerateInterpolator
import cn.refactor.lib.colordialog.PromptDialog
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.util.Constant
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.createFinishListener
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.showDialogImmersive
import cz.muni.fi.umimecesky.prefs

class TrainingProgressBar(private val activity: Activity, private val progressBar: RoundCornerProgressBar) {

    private val isInfinite: Boolean
    private var seriesLength: Int = 0

    private var passedWords = 0
    private var incorrectAttempts = 0

    init {

        val seriesAmountString = prefs.seriesAmount
        if (Constant.INFINITY == seriesAmountString) {
            isInfinite = true
            progressBar.visibility = View.GONE
        } else {
            isInfinite = false
            progressBar.visibility = View.VISIBLE
            seriesLength = seriesAmountString.toInt()
            updateProgressBar()
        }
    }

    fun incrementIncorrectAttemptsCounter() {
        incorrectAttempts++
    }

    fun processCorrectResult() {
        passedWords++

        if (!isInfinite) {
            updateProgressBar()

            if (passedWords == seriesLength) {
                showSeriesFinishedDialog()
            }
        }
    }

    private fun updateProgressBar() {
        val currentProgressPercentage = calculateFinishedPercentage()
        val currentProgress = currentProgressPercentage / 100.0f

        animate(currentProgress)

        progressBar.progress = currentProgress
        progressBar.secondaryProgress = progressBar.progress + SECONDARY_PROGRESS_SHIFT

        updateProgressBarColors()
    }

    private fun calculateFinishedPercentage(): Int = passedWords * 100 / seriesLength

    private fun animate(to: Float) {
        val animation = ObjectAnimator.ofFloat(progressBar, "progress", progressBar.progress, to)
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        animation.start()

        val animation2 = ObjectAnimator.ofFloat(progressBar, "secondaryProgress", progressBar.secondaryProgress, to + SECONDARY_PROGRESS_SHIFT)
        animation2.duration = 650
        animation2.interpolator = DecelerateInterpolator()
        animation2.start()

    }

    private fun updateProgressBarColors() {
        val currentProgressPercentage = calculateFinishedPercentage()
        when {
            currentProgressPercentage <= 30 -> updateColors(R.color.progress_red_progress, R.color.progress_red_progress_half)
            currentProgressPercentage <= 65 -> updateColors(R.color.progress_orange_progress, R.color.progress_orange_progress_half)
            else -> updateColors(R.color.color_primary, R.color.color_primary_half)
        }
    }

    private fun updateColors(colorReference: Int, secondaryColorReference: Int) {
        progressBar.progressColor = ContextCompat.getColor(activity, colorReference)
        progressBar.secondaryProgressColor = ContextCompat.getColor(activity, secondaryColorReference)
    }

    private fun showSeriesFinishedDialog() {
        val promptDialog = PromptDialog(activity)
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                .setTitleText(activity.getString(R.string.series_finished))
                .setContentText(createDialogContent())
                .setPositiveListener(R.string.ok, activity.createFinishListener())
                .setCanceledOnTouchOutside(false)
        activity.showDialogImmersive(promptDialog)
    }

    private fun createDialogContent(): String {
        return activity.getString(R.string.success_rate) +
                " " +
                calculateSuccessPercentage() + "%." +
                "\n" +
                dialogSuccessMessage()
    }

    private fun dialogSuccessMessage(): String {
        val percentage = calculateSuccessPercentage()
        return when {
            percentage >= 80 -> activity.getString(R.string.great_job)
            percentage >= 60 -> activity.getString(R.string.its_ok)
            else -> activity.getString(R.string.you_need_to_improve)
        }
    }

    private fun calculateSuccessPercentage(): Int =
            (passedWords - incorrectAttempts) * 100 / passedWords

    companion object {
        private const val SECONDARY_PROGRESS_SHIFT = 0.05f
    }

}
