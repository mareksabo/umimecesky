package cz.muni.fi.umimecesky.pojo

import cz.muni.fi.umimecesky.logic.RobotLogic

/**
 * @author Marek Sabo
 */
data class Robot(val logic: RobotLogic?) {

    constructor() : this(null)

    @Volatile var lastWasCorrect = true

    val hopsPerCorrect: Int
        get() = logic?.hopsPerCorrect() ?: 1

    val isAI: Boolean
        get() = logic != null

    fun canMove() = if (isAI) RobotLogic.probabilityTrue(logic!!.correctnessRatio()) else false

}