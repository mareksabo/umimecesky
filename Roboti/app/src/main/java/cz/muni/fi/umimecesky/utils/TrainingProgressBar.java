package cz.muni.fi.umimecesky.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;

public class TrainingProgressBar {

    private static final float SECONDARY_PROGRESS_SHIFT = 0.05f;

    private final Activity activity;
    private final RoundCornerProgressBar progressBar;

    private final boolean isInfinite;
    private int seriesLength;

    private int passedWords = 0;
    private int incorrectAttempts = 0;

    public TrainingProgressBar(Activity activity, RoundCornerProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;

        String seriesLengthString = Util.getSharedPreferences(activity).getString(Constant.LAST_SPINNER_VALUE, Constant.INFINITY);
        if (Constant.INFINITY.equals(seriesLengthString)) {
            isInfinite = true;
            progressBar.setVisibility(View.GONE);
        } else {
            isInfinite = false;
            progressBar.setVisibility(View.VISIBLE);
            seriesLength = Integer.parseInt(seriesLengthString);
            updateProgressBar(calculateFinishedPercentage());
        }
    }

    private int calculateFinishedPercentage() {
        return passedWords * 100 / seriesLength;
    }

    public void incrementIncorrectAttemptsCounter() {
        incorrectAttempts++;
    }

    public void processCorrectResult() {
        passedWords++;

        if (!isInfinite) {
            updateProgressBar(calculateFinishedPercentage());

            if (passedWords == seriesLength) {
                showSeriesFinishedDialog();
            }
        }
    }


    private void updateProgressBar(int currentProgressPercentage) {
        float currentProgress = currentProgressPercentage / 100.0f;

        animate(currentProgress);

        progressBar.setProgress(currentProgress);
        progressBar.setSecondaryProgress(progressBar.getProgress() + SECONDARY_PROGRESS_SHIFT);

        updateProgressBarColors(currentProgressPercentage);
    }

    private void animate(float to) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(progressBar, "progress", progressBar.getProgress(), to);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        ObjectAnimator animation2 = ObjectAnimator.ofFloat(progressBar, "secondaryProgress", progressBar.getSecondaryProgress(), to + SECONDARY_PROGRESS_SHIFT);
        animation2.setDuration(650);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.start();

    }

    private void updateProgressBarColors(int currentProgressPercentage) {
        if (currentProgressPercentage <= 30) {
            updateColors(R.color.progress_red_progress, R.color.progress_red_progress_half);
        } else if (currentProgressPercentage <= 65) {
            updateColors(R.color.progress_orange_progress, R.color.progress_orange_progress_half);
        } else {
            updateColors(R.color.color_primary, R.color.color_primary_half);
        }
    }

    private void updateColors(int colorReference, int secondaryColorReference) {
        progressBar.setProgressColor(ContextCompat.getColor(activity, colorReference));
        progressBar.setSecondaryProgressColor(ContextCompat.getColor(activity, secondaryColorReference));
    }

    private void showSeriesFinishedDialog() {
        final PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                .setTitleText(activity.getString(R.string.series_finished))
                .setContentText(activity.getString(R.string.success_rate) + calculateSuccessPercentage() + "%.\n"
                        + dialogSuccessMessage())
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(activity))
                .setCanceledOnTouchOutside(false);
        GuiUtil.showDialogImmersive(promptDialog, activity);
    }

    private String dialogSuccessMessage() {
        int percentage = calculateSuccessPercentage();
        if (percentage >= 80) {
            return activity.getString(R.string.great_job);
        } else if (percentage >= 60) {
            return activity.getString(R.string.its_ok);
        } else {
            return activity.getString(R.string.you_need_to_improve);
        }
    }

    private int calculateSuccessPercentage() {
        return ((passedWords - incorrectAttempts) * 100) / passedWords;
    }

}
