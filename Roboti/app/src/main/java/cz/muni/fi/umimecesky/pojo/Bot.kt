package cz.muni.fi.umimecesky.pojo

import android.widget.ImageView

import cz.muni.fi.umimecesky.logic.RobotLogic
import cz.muni.fi.umimecesky.utils.Util

class Bot(view: ImageView, val logic: RobotLogic) : AbstractRobot(view, logic.hopsPerCorrect()) {

    fun processBotMove() {
        val canMove = Util.probabilityTrue(logic.correctnessRatio())
        if (canMove) {
            applyCorrect()
        } else {
            applyWrong()
        }
    }

    override fun applyCorrect() {
        moveForward()
    }

    override fun applyWrong() {
        if (lastWasCorrect) { // bot moves back only once
            moveBackward()
        }
    }
}
