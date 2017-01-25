package cz.muni.fi.umimecesky.roboti.utils;

class BotLogicQuick implements RobotLogic {

    @Override
    public int milisecondsPerSolution() {
        return 2500;
    }

    @Override
    public double correctnessRatio() {
        return 0.8;
    }

}
