package cz.muni.fi.umimecesky.pojo;

import android.util.Log;
import android.widget.ImageView;

import cz.muni.fi.umimecesky.utils.CalculateDp;
import cz.muni.fi.umimecesky.utils.Global;
import cz.muni.fi.umimecesky.utils.RobotLogic;
import cz.muni.fi.umimecesky.utils.Util;

public class Bot {

    private ImageView view;
    private RobotLogic logic;
    private boolean isWrong;

    private int remainingHopsToWin;
    private CalculateDp calculateDp;
    private final float ROBOT_MOVE;

    public Bot(ImageView view, RobotLogic logic) {
        this.view = view;
        this.logic = logic;
        isWrong = false;
        calculateDp = Global.getCalculateDp();
        remainingHopsToWin = calculateDp.getWinMovesCount();
        ROBOT_MOVE = calculateDp.calculateRobotMovePx();
    }

    public ImageView getView() {
        return view;
    }

    public RobotLogic getLogic() {
        return logic;
    }

    public boolean processBotMove() {
        boolean canMove = Util.probabilityTrue(getLogic().correctnessRatio());
        if (canMove) {
            moveForward();
        } else {
            if (!isWrong) { // bot moves back only once
                moveBackward();
            }
        }
        return isWinner();
    }


    public void moveForward() {
        Log.v("forward", String.valueOf(logic));
        if (isWrong) {
            isWrong = false;
            return;
        }
        int hopsPerCorrect = (logic == null) ? 1 : logic.hopsPerCorrect();
        processMoveForward(hopsPerCorrect);
    }

    private void processMoveForward(int hopsPerCorrect) {
        hopsPerCorrect = limitHopsWhenWon(hopsPerCorrect);
        remainingHopsToWin -= hopsPerCorrect;
        getView().animate().translationXBy(ROBOT_MOVE * hopsPerCorrect);
    }

    private int limitHopsWhenWon(int hopsBefore) {
        if (remainingHopsToWin >= hopsBefore) {
            return hopsBefore;
        } else {
            return remainingHopsToWin;
        }
    }

    public void moveBackward() {
        isWrong = true;
        if (remainingHopsToWin != calculateDp.getWinMovesCount()) {
            processMoveBackward();
        }
    }

    private void processMoveBackward() {
        remainingHopsToWin++;
        getView().animate().translationXBy(-ROBOT_MOVE);
    }

    @Override
    public String toString() {
        return "Bot{" +
                "view=" + view +
                ", logic=" + logic +
                ", isWrong=" + isWrong +
                '}';
    }

    /**
     * Checks if robot has won, after calling its move. Move can be still processed.
     * @return true if robot is beyond finish line
     */
    public boolean isWinner() {
        return remainingHopsToWin <= 0;
    }

}
