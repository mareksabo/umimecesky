package cz.muni.fi.umimecesky.game.flappy

import android.app.Activity
import android.os.Bundle
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.game.shared.util.Constant.FLAPPY_CHOSEN_CATEGORY

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
