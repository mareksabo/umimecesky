package cz.muni.fi.umimecesky.flappygame

import android.app.Activity
import android.os.Bundle
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.utils.Constant.FLAPPY_CHOSEN_CATEGORY

/**
 * @author Marek Sabo
 */
class JumpGameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val raceConcept = intent.getSerializableExtra(FLAPPY_CHOSEN_CATEGORY) as RaceConcept
        setContentView(GameLogic(this, raceConcept))
    }
}
