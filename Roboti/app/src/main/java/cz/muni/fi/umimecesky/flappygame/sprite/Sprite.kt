package cz.muni.fi.umimecesky.flappygame.sprite

import android.app.Activity
import android.graphics.Canvas
import cz.muni.fi.umimecesky.ballgame.Dimensions
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * @author Marek Sabo
 */
interface Sprite {

    val defaultMoveX: Int
        get() = Dimensions.displayWidth() / 200

    /**
     * Draws sprite on canvas.
     */
    fun draw(canvas: Canvas)

    /**
     * Moves the sprite.
     */
    fun move()

    /**
     * Called when level is restarted, gets to the initial state, f.e. it resets the position.
     */
    fun reset()

    /**
     * Does intro if available, otherwise returns null.
     */
    fun intro(activity: Activity): FancyShowCaseView?
}