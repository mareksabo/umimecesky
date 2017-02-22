package cz.muni.fi.umimecesky.roboti.utils;

import android.util.Log;

import cz.muni.fi.umimecesky.roboti.pojo.RaceConcept;

class BotLogicSlow implements RobotLogic {

    private RaceConcept concept;

    public BotLogicSlow(RaceConcept concept) {
        this.concept = concept;
        Log.v("slow concept", String.valueOf(concept.levelProgress()));
        Log.v("slow is before half", String.valueOf(isBeforeHalf()));
    }

    @Override
    public int millisecondsPerSolution() {
        return 3500 - (int) (1500 * concept.levelProgress());
    }

    @Override
    public double correctnessRatio() {
        double addition = isBeforeHalf() ? Utils.roundBy2(0.4 * concept.levelProgress()) :
                0.1 * concept.levelProgress();
        return 0.75 + addition;
    }

    @Override
    public int hopsPerCorrect() {
        return isBeforeHalf() ? 1 : 2;
    }

    private boolean isBeforeHalf() {
        return concept.levelProgress() < 0.5;
    }
}
