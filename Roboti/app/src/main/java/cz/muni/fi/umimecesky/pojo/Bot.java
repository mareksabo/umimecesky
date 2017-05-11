package cz.muni.fi.umimecesky.pojo;

import android.widget.ImageView;

import cz.muni.fi.umimecesky.logic.RobotLogic;
import cz.muni.fi.umimecesky.utils.Util;

public class Bot extends AbstractRobot {

    private final RobotLogic logic;

    public Bot(ImageView view, RobotLogic logic) {
        super(view, logic.hopsPerCorrect());
        this.logic = logic;
    }

    public void processBotMove() {
        boolean canMove = Util.probabilityTrue(getLogic().correctnessRatio());
        if (canMove) {
            applyCorrect();
        } else {
            applyWrong();
        }
    }


    public RobotLogic getLogic() {
        return logic;
    }

    @Override
    public void applyCorrect() {
        moveForward();
    }

    @Override
    public void applyWrong() {
        if (getLastWasCorrect()) { // bot moves back only once
            moveBackward();
        }
    }
}
