package cz.muni.fi.umimecesky.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.DecelerateInterpolator

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar

import cn.refactor.lib.colordialog.PromptDialog
import cz.muni.fi.umimecesky.R

class TrainingProgressBar(private val activity: Activity, private val progressBar: RoundCornerProgressBar) {

    private val isInfinite: Boolean
    private var seriesLength: Int = 0

    private var passedWords = 0
    private var incorrectAttempts = 0

    init {

        val seriesLengthString = Util.getSharedPreferences(activity).getString(Constant.LAST_SPINNER_VALUE, Constant.INFINITY)
        if (Constant.INFINITY == seriesLengthString) {
            isInfinite = true
            progressBar.visibility = View.GONE
        } else {
            isInfinite = false
            progressBar.visibility = View.VISIBLE
            seriesLength = Integer.parseInt(seriesLengthString)
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

    private fun calculateFinishedPercentage(): Int {
        return passedWords * 100 / seriesLength
    }

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
        if (currentProgressPercentage <= 30) {
            updateColors(R.color.progress_red_progress, R.color.progress_red_progress_half)
        } else if (currentProgressPercentage <= 65) {
            updateColors(R.color.progress_orange_progress, R.color.progress_orange_progress_half)
        } else {
            updateColors(R.color.color_primary, R.color.color_primary_half)
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
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(activity))
                .setCanceledOnTouchOutside(false)
        GuiUtil.showDialogImmersive(promptDialog, activity)
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
        if (percentage >= 80) {
            return activity.getString(R.string.great_job)
        } else if (percentage >= 60) {
            return activity.getString(R.string.its_ok)
        } else {
            return activity.getString(R.string.you_need_to_improve)
        }
    }

    private fun calculateSuccessPercentage(): Int {
        return (passedWords - incorrectAttempts) * 100 / passedWords
    }

    companion object {

        private val SECONDARY_PROGRESS_SHIFT = 0.05f
    }

}
