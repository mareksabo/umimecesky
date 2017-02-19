package cz.muni.fi.umimecesky.roboti.pojo;

import android.util.Log;
import android.widget.ImageView;

import cz.muni.fi.umimecesky.roboti.utils.CalculateDp;
import cz.muni.fi.umimecesky.roboti.utils.Global;
import cz.muni.fi.umimecesky.roboti.utils.RobotLogic;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

public class Bot {

    private ImageView view;
    private RobotLogic logic;
    private boolean isWrong;

    private CalculateDp calculateDp;
    private final float ROBOT_MOVE;

    public Bot(ImageView view, RobotLogic logic) {
        this.view = view;
        this.logic = logic;
        isWrong = false;
        calculateDp = Global.getCalculateDp();
        ROBOT_MOVE = calculateDp.calculateRobotMovePx();
    }

    public ImageView getView() {
        return view;
    }

    public RobotLogic getLogic() {
        return logic;
    }

    public boolean processBotMove() {
        boolean canMove = Utils.probabilityTrue(getLogic().correctnessRatio());
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
        getView().animate().translationXBy(ROBOT_MOVE);
        Log.i("getX", String.valueOf(getView().getX()));
    }

    public void moveBackward() {
        Log.v("backward", String.valueOf(logic));
        isWrong = true;
        if (getView().getX() - ROBOT_MOVE < 0) {
            return;
        }
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

    public boolean isWinner() {
        Log.i("iswinner", String.valueOf(getView().getX()));
        return calculateDp.isBeyondFinishLine(getView().getX());
    }

}
