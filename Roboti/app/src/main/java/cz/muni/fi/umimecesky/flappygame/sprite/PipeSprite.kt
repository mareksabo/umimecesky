package cz.muni.fi.umimecesky.flappygame.sprite

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.flappygame.BitmapHelper
import cz.muni.fi.umimecesky.flappygame.screenHeight
import cz.muni.fi.umimecesky.flappygame.screenWidth
import java.util.*

/**
 * @author Marek Sabo
 */
class PipeSprite(resources: Resources) {

    companion object {
        private const val GAP_HEIGHT = 300
        private const val WIDTH = 200
        private val HEIGHT = screenHeight / 4
        private val MID_HEIGHT = 2 * HEIGHT - GAP_HEIGHT
        private val beforeScreenX = screenWidth.toFloat()
    }

    private val topPipe = BitmapHelper.generateImage(resources, R.drawable.pipe_down, WIDTH, HEIGHT)
    private val midPipe = BitmapHelper.generateImage(resources, R.drawable.pipe_mid, WIDTH, MID_HEIGHT)
    private val botPipe = BitmapHelper.generateImage(resources, R.drawable.pipe_up, WIDTH, HEIGHT)

    private var shiftY: Float = generateShiftY()

    private var currX: Float = beforeScreenX
        set(value) {
            field = value
            if (value < -WIDTH) {
                field = beforeScreenX
                shiftY = generateShiftY()
            }
        }

    private fun generateShiftY() = Random().nextInt(200) - 100f

    private fun topPipeShiftY() = shiftY - GAP_HEIGHT / 2
    private fun midPipeShiftY() = topPipeShiftY() + HEIGHT + GAP_HEIGHT
    private fun botPipeShiftY() = midPipeShiftY() + MID_HEIGHT + GAP_HEIGHT

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(topPipe, currX, topPipeShiftY(), null)
        canvas.drawBitmap(midPipe, currX, midPipeShiftY(), null)
        canvas.drawBitmap(botPipe, currX, botPipeShiftY(), null)
    }

    fun move() {
        currX -= 10 // move closer to left
    }

    fun resetPosition() {
        currX = beforeScreenX
    }

    fun createTopRect(): Rect =
            createRect(currX.toInt(), topPipeShiftY().toInt(), WIDTH, HEIGHT)

    fun createMidRect() =
            createRect(currX.toInt(), midPipeShiftY().toInt(), WIDTH, MID_HEIGHT)

    fun createBotRect() =
            createRect(currX.toInt(), botPipeShiftY().toInt(), WIDTH, HEIGHT)

    private fun createRect(pipeX: Int, pipeY: Int, pipeWidth: Int, pipeHeight: Int) =
            Rect(pipeX, pipeY, pipeX + pipeWidth, pipeY + pipeHeight)
}
