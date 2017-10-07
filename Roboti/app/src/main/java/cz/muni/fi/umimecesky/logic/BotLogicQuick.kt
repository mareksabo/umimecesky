package cz.muni.fi.umimecesky.logic

import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.utils.Util

class BotLogicQuick(private val concept: RaceConcept) : RobotLogic {

    override fun millisecondsPerSolution(): Int {
        return 3000 - (1800 * concept.levelProgress()).toInt()
    }

    override fun correctnessRatio(): Double {
        return 0.65 + Util.roundBy2places(0.3 * concept.levelProgress())
    }

    override fun hopsPerCorrect(): Int {
        return 1
    }

}
