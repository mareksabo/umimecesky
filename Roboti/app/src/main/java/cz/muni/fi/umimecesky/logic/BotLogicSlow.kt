package cz.muni.fi.umimecesky.logic

import android.util.Log

import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.utils.Util

class BotLogicSlow(private val concept: RaceConcept) : RobotLogic {

    init {
        Log.v("slow concept", concept.levelProgress().toString())
        Log.v("slow is before half", isBeforeHalf.toString())
    }

    override fun millisecondsPerSolution(): Int {
        return 3500 - (1500 * concept.levelProgress()).toInt()
    }

    override fun correctnessRatio(): Double {
        val addition = if (isBeforeHalf)
            Util.roundBy2places(0.4 * concept.levelProgress())
        else
            0.1 * concept.levelProgress()
        return 0.75 + addition
    }

    override fun hopsPerCorrect(): Int {
        return if (isBeforeHalf) 1 else 2
    }

    private val isBeforeHalf: Boolean
        get() = concept.levelProgress() < 0.5
}
