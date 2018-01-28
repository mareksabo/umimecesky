package cz.muni.fi.umimecesky.logic

import cz.muni.fi.umimecesky.logic.RobotLogic.Companion.roundBy2places
import cz.muni.fi.umimecesky.pojo.RaceConcept

class BotLogicQuick(private val concept: RaceConcept) : RobotLogic {

    override fun millisecondsPerSolution(): Int = 3000 - (1800 * concept.levelProgress()).toInt()

    override fun correctnessRatio(): Double =
            0.65 + roundBy2places(0.3 * concept.levelProgress())

    override fun hopsPerCorrect(): Int = 1

}
