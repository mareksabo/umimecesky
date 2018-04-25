package cz.muni.fi.umimecesky.flappygame

import android.app.Activity
import android.os.Bundle

/**
 * @author Marek Sabo
 */
class JumpGameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView(this))
    }
}
