package cz.muni.fi.umimecesky.utils;

public interface RobotLogic {

    /**
     * Returns how many miliseconds it takes to solve clue.
     * @return positive number
     */
    int millisecondsPerSolution();

    /**
     * Checks how often is robot correct.
     * @return number between 0 and 1
     */
    double correctnessRatio();

    /**
     * How many hops should robot do when correct.
     * @return positive number, usually 1 or 2
     */
    int hopsPerCorrect();
}
