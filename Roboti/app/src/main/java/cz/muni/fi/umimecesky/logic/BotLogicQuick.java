package cz.muni.fi.umimecesky.logic;

import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.utils.Util;

public class BotLogicQuick implements RobotLogic {

    private RaceConcept concept;

    public BotLogicQuick(RaceConcept concept) {
        this.concept = concept;
    }

    @Override
    public int millisecondsPerSolution() {
        return 3000 - (int) (1800 * concept.levelProgress());
    }

    @Override
    public double correctnessRatio() {
        return 0.65 + Util.roundBy2places(0.3 * concept.levelProgress());
    }

    @Override
    public int hopsPerCorrect() {
        return 1;
    }

}
