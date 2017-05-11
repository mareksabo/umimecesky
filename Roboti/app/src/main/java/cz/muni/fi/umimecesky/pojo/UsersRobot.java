package cz.muni.fi.umimecesky.pojo;

import android.widget.ImageView;

public class UsersRobot extends AbstractRobot {

    public UsersRobot(ImageView view) {
        super(view, 1);
    }

    @Override
    public void applyCorrect() {
        if (getLastWasCorrect()) super.moveForward();
        setLastWasCorrect(true);
    }

    @Override
    public void applyWrong() {
        moveBackward();
    }
}
