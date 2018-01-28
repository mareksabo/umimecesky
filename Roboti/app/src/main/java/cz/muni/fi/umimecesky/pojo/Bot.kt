package cz.muni.fi.umimecesky.pojo

import android.widget.ImageView
import cz.muni.fi.umimecesky.logic.RobotLogic
import cz.muni.fi.umimecesky.logic.RobotLogic.Companion.probabilityTrue

class Bot(view: ImageView, val logic: RobotLogic) : AbstractRobot(view, logic.hopsPerCorrect()) {

    fun processBotMove() {
        val canMove = probabilityTrue(logic.correctnessRatio())
        if (canMove) {
            applyCorrect()
        } else {
            applyWrong()
        }
    }

    override fun applyCorrect() = moveForward()

    override fun applyWrong() {
        // bot moves back only once
        if (lastWasCorrect) moveBackward()
    }
}
