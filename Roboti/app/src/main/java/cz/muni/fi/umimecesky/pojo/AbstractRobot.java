package cz.muni.fi.umimecesky.pojo;

import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

import cz.muni.fi.umimecesky.utils.CalculateDp;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.Global;

public abstract class AbstractRobot {

    private Runnable runnableBefore;
    private Runnable runnableAfter;

    public static AtomicBoolean noWinnerYet;

    private int hopsPerCorrect = 1;
    private volatile boolean lastWasCorrect = true;
    private int remainingHopsToWin;
    private ViewPropertyAnimator animator;

    private final float ROBOT_MOVE;
    private final int TOTAL_MOVES_TO_WIN;

    public abstract void applyCorrect();

    public abstract void applyWrong();

    public AbstractRobot(ImageView view, int hopsPerCorrect) {
        this.hopsPerCorrect = hopsPerCorrect;

        animator = view.animate();
        animator.setDuration(Constant.ANIMATION_DURATION);

        CalculateDp calculateDp = Global.getCalculateDp();
        ROBOT_MOVE = calculateDp.calculateRobotMovePx();

        TOTAL_MOVES_TO_WIN = calculateDp.getWinMovesCount();
        remainingHopsToWin = TOTAL_MOVES_TO_WIN;
    }

    protected final void moveForward() {
        lastWasCorrect = true;
        if (isOneStepToWin()) {
            decideAnimationForward();
        } else if (noWinnerYet.get()) {
            decideAnimationForward();
        }
    }

    private void decideAnimationForward() {
        if (isOneStepToWin()) {
            animateForwardWithAction(runnableBefore, runnableAfter);
        } else {
            animateForward();
        }
        remainingHopsToWin -= limitedHopsPerCorrect();
    }

    protected final void moveBackward() {
        if (lastWasCorrect) {
            lastWasCorrect = false;

            if (isNotAtStart()) {
                animateBackward();
            }
        }
    }

    private void animateBackward() {
        animator.translationXBy(-ROBOT_MOVE);
        remainingHopsToWin++;
    }

    private void animateForward() {
        animateForwardWithAction(null, null);
    }

    protected void animateForwardWithAction(Runnable runnableBefore, Runnable runnableAfter) {
        animator.translationXBy(ROBOT_MOVE * limitedHopsPerCorrect())
                .withStartAction(runnableBefore)
                .withEndAction(runnableAfter);
    }

    private int limitedHopsPerCorrect() {
        return limitHopsWhenWon(hopsPerCorrect);
    }

    private int limitHopsWhenWon(int hopsBefore) {
        if (remainingHopsToWin >= hopsBefore) {
            return hopsBefore;
        } else {
            return remainingHopsToWin;
        }

    }

    protected void setLastWasCorrect(boolean lastWasCorrect) {
        this.lastWasCorrect = lastWasCorrect;
    }

    protected boolean getLastWasCorrect() {
        return lastWasCorrect;
    }

    private boolean isOneStepToWin() {
        return 0 < remainingHopsToWin && remainingHopsToWin <= hopsPerCorrect;
    }

    private boolean isNotAtStart() {
        return remainingHopsToWin != TOTAL_MOVES_TO_WIN;
    }

    public void setRunnableAfter(Runnable runnableAfter) {
        this.runnableAfter = runnableAfter;
    }

    public void setRunnableBefore(Runnable runnableBefore) {
        this.runnableBefore = runnableBefore;
    }
}
