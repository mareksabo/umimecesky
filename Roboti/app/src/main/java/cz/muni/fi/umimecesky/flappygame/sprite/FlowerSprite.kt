package cz.muni.fi.umimecesky.flappygame.sprite

import android.app.Activity
import android.content.res.Resources
import android.graphics.Canvas
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayHeight
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayWidth
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * Flower has fixed Y value, X is moving towards the users' sprite.
 *
 * @author Marek Sabo
 */
class FlowerSprite(resources: Resources) : Sprite {

    companion object {
        private const val WIDTH = 240
        private const val HEIGHT = 415
        private val beforeScreenX = displayWidth().toFloat()
        private val startingX = beforeScreenX / 2
        private val STARTING_Y = displayHeight() - HEIGHT.toFloat()
    }

    private val image = GraphicsHelper.generateImage(resources, R.drawable.flower, WIDTH, HEIGHT)

    private var currX: Float = startingX
        set(value) {
            field = value
            if (value < -WIDTH) field = beforeScreenX
        }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, currX, STARTING_Y, null)
    }

    override fun move() {
        currX -= 10 // move closer to left
    }

    override fun reset() {
        currX = startingX
    }


    override fun intro(activity: Activity): FancyShowCaseView? = null
}
