package cz.muni.fi.umimecesky.roboti.pojo;

import android.util.Log;
import android.widget.ImageView;

import cz.muni.fi.umimecesky.roboti.utils.RobotLogic;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.ROBOT_MOVE;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.WINNING_X_COORDINATES;

public class Bot {

    private ImageView view;
    private RobotLogic logic;
    private boolean isWrong;

    public Bot(ImageView view, RobotLogic logic) {
        this.view = view;
        this.logic = logic;
        isWrong = false;
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
        return getView().getX() >= WINNING_X_COORDINATES;
    }

}
