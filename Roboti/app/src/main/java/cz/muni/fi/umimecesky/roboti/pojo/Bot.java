package cz.muni.fi.umimecesky.roboti.pojo;

import android.util.Log;
import android.widget.ImageView;

import cz.muni.fi.umimecesky.roboti.utils.RobotLogic;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.ROBOT_MOVE;

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
            moveBackward();
        }
        return canMove;
    }


    public void moveForward() {
        if (isWrong) {
            isWrong = false;
            return;
        }
        getView().animate().translationXBy(ROBOT_MOVE);

        Log.i("getX", String.valueOf(getView().getX()));
    }

    public void moveBackward() {
        isWrong = true;
        if (getView().getX() - ROBOT_MOVE < 0) {
            return;
        }
        getView().animate().translationXBy(-ROBOT_MOVE);
    }

}
