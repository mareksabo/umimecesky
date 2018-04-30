package cz.muni.fi.umimecesky.game.robots.logic

import cz.muni.fi.umimecesky.game.robots.logic.RobotLogic.Companion.roundBy2places
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept

class BotLogicQuick(private val concept: RaceConcept) : RobotLogic {

    override fun millisecondsPerSolution(): Long = 3000L - (1800L * concept.levelProgress()).toLong()

    override fun correctnessRatio(): Double =
            0.65 + roundBy2places(0.3 * concept.levelProgress())

    override fun hopsPerCorrect(): Int = 1

}