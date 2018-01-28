package cz.muni.fi.umimecesky.pojo

import android.widget.ImageView

class UsersRobot(view: ImageView) : AbstractRobot(view, 1) {

    override fun applyCorrect() {
        if (lastWasCorrect) super.moveForward()
        lastWasCorrect = true
    }

    override fun applyWrong() = moveBackward()
}
