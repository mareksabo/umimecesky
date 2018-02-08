package cz.muni.fi.umimecesky.logic

interface RobotLogic {

    /**
     * Returns how many milliseconds it takes to solve clue.
     * @return positive number
     */
    fun millisecondsPerSolution(): Long

    /**
     * Checks how often is robot correct.
     * @return number between 0 and 1
     */
    fun correctnessRatio(): Double

    /**
     * How many hops should robot do when correct.
     * @return positive number, usually 1 or 2
     */
    fun hopsPerCorrect(): Int

    companion object {
        fun probabilityTrue(probabilityToReturnTrue: Double): Boolean =
                Math.random() >= 1.0 - probabilityToReturnTrue

        fun roundBy2places(numberToRound: Double): Double =
                Math.round(numberToRound * 100).toDouble() / 100
    }
}
