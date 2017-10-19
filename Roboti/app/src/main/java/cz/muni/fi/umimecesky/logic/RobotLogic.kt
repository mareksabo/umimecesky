package cz.muni.fi.umimecesky.logic

interface RobotLogic {

    /**
     * Returns how many milliseconds it takes to solve clue.
     * @return positive number
     */
    fun millisecondsPerSolution(): Int

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
}
