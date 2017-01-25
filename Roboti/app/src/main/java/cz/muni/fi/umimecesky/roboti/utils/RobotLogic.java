package cz.muni.fi.umimecesky.roboti.utils;

public interface RobotLogic {

    /**
     * Returns how many miliseconds it takes to solve clue.
     * @return positive number
     */
    int milisecondsPerSolution();

    /**
     * Checks how often is robot correct.
     * @return number between 0 and 1
     */
    double correctnessRatio();

}
