package cz.muni.fi.umimecesky.flappygame.sprite

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayHeight
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper

/**
 * Bee has its X position same all the time, only Y is changing.
 *
 * @author Marek Sabo
 */
class BeeSprite(resources: Resources) {

    companion object {
        private const val WIDTH = 150
        private const val HEIGHT = 130
        private const val MAX_VELOCITY = 6

        const val STARTING_X = 100f
        private const val STARTING_Y = 100f
    }

    private val image = GraphicsHelper.generateImage(resources, R.drawable.bee, WIDTH, HEIGHT)

    private var currY: Float = STARTING_Y
    private var velocityY: Int = MAX_VELOCITY
        set(value) = if (value <= MAX_VELOCITY) field = value else {}

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, STARTING_X, currY, null)
    }

    fun move() {
        currY += velocityY
        velocityY += 2
    }

    fun doJump() {
        velocityY = -20
        currY -= 10
    }

    fun resetPosition() {
        currY = STARTING_Y
    }

    fun isOutsideScreen() = displayHeight() < currY || currY < -HEIGHT

    fun createRect(): Rect {
        val beeX = STARTING_X.toInt()
        val beeY = currY.toInt()

        return Rect(beeX, beeY, beeX + WIDTH, beeY + HEIGHT)
    }
}


