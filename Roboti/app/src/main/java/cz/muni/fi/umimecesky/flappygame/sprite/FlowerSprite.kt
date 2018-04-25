package cz.muni.fi.umimecesky.flappygame.sprite

import android.content.res.Resources
import android.graphics.Canvas
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.flappygame.BitmapHelper
import cz.muni.fi.umimecesky.flappygame.screenHeight
import cz.muni.fi.umimecesky.flappygame.screenWidth

/**
 * Flower has fixed Y value, X is moving towards the users' sprite.
 *
 * @author Marek Sabo
 */
class FlowerSprite(resources: Resources) {

    companion object {
        private const val WIDTH = 240
        private const val HEIGHT = 415
        private val beforeScreenX = screenWidth.toFloat()
        private val startingX = beforeScreenX / 2
    }

    private val image = BitmapHelper.generateImage(resources, R.drawable.flower, WIDTH, HEIGHT)

    private var currX: Float = startingX
        set(value) {
            field = value
            if (value < -WIDTH) field = beforeScreenX
        }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, currX, screenHeight - HEIGHT.toFloat(), null)
    }

    fun move() {
        currX -= 10 // move closer to left
    }

    fun resetPosition() {
        currX = startingX
    }

}
