package cz.muni.fi.umimecesky.roboti.utils;

class BotLogicSlow implements RobotLogic {
    @Override
    public int milisecondsPerSolution() {
        return 5000;
    }

    @Override
    public double correctnessRatio() {
        return 0.75;
    }
}
