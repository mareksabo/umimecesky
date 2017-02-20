package cz.muni.fi.umimecesky.roboti.utils;

import cz.muni.fi.umimecesky.roboti.pojo.RaceConcept;

class BotLogicQuick implements RobotLogic {

    private RaceConcept concept;

    public BotLogicQuick(RaceConcept concept) {
        this.concept = concept;
    }

    @Override
    public int millisecondsPerSolution() {
        return 3500 - (int) (2000 * concept.levelProgress());
    }

    @Override
    public double correctnessRatio() {
        return 0.65 + Utils.roundBy2(0.3 * concept.levelProgress());
    }

    @Override
    public int hopsPerCorrect() {
        return 1;
    }

}
