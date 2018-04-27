package cz.muni.fi.umimecesky.flappygame

import android.app.Activity
import cz.muni.fi.umimecesky.flappygame.sprite.BeeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FlowerSprite
import cz.muni.fi.umimecesky.flappygame.sprite.PipeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.Sprite
import me.toptas.fancyshowcase.FancyShowCaseQueue


/**
 * @author Marek Sabo
 */
class GameIntroduce(private val activity: Activity, sprites: List<Sprite>, onComplete: () -> Unit) {

    init {
        val queue = FancyShowCaseQueue()
                .add(sprites.first { it is BeeSprite }.run { this.intro(activity) })
                .add(sprites.first { it is PipeSprite }.run { this.intro(activity) })
                .add(sprites.first { it is FlowerSprite }.run { this.intro(activity) })
        queue.setCompleteListener { onComplete() }
        queue.show()
    }

}