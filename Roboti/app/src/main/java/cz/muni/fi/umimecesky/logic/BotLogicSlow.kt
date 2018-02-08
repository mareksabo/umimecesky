package cz.muni.fi.umimecesky.logic

import cz.muni.fi.umimecesky.logic.RobotLogic.Companion.roundBy2places
import cz.muni.fi.umimecesky.pojo.RaceConcept

class BotLogicSlow(private val concept: RaceConcept) : RobotLogic {

    override fun millisecondsPerSolution(): Long = 3500L - (1500L * concept.levelProgress()).toLong()

    override fun correctnessRatio(): Double {
        val addition = if (isBeforeHalf)
            roundBy2places(0.4 * concept.levelProgress())
        else
            0.1 * concept.levelProgress()
        return 0.75 + addition
    }

    override fun hopsPerCorrect(): Int = if (isBeforeHalf) 1 else 2

    private val isBeforeHalf: Boolean
        get() = concept.levelProgress() < 0.5
}
