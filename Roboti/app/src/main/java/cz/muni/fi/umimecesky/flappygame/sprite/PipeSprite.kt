package cz.muni.fi.umimecesky.flappygame.sprite

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayHeight
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayWidth
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper
import java.util.*

/**
 * @author Marek Sabo
 */
class PipeSprite(resources: Resources) {

    companion object {
        private const val GAP_HEIGHT = 300
        private const val WIDTH = 200
        private val HEIGHT = displayHeight() / 4
        private val MID_HEIGHT = 2 * HEIGHT - GAP_HEIGHT
        private val beforeScreenX = displayWidth().toFloat()

        private val paint = GraphicsHelper.createAnswersPaint()
    }

    var answers: Pair<String, String> = Pair("", "")

    private var canChangeWord = true

    private val topPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_down, WIDTH, HEIGHT)
    private val midPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_mid, WIDTH, MID_HEIGHT)
    private val botPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_up, WIDTH, HEIGHT)

    private var shiftY: Float = generateShiftY()

    private var currX: Float = beforeScreenX
        set(value) {
            field = value
            if (value < -WIDTH) {
                field = beforeScreenX
                shiftY = generateShiftY()
                canChangeWord = true
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

        canvas.drawText(answers.first, currX + WIDTH / 2, midPipeShiftY() - GAP_HEIGHT / 2, paint)
        canvas.drawText(answers.second, currX + WIDTH / 2, botPipeShiftY() - GAP_HEIGHT / 2, paint)
    }

    fun move() {
        currX -= 10 // move closer to left
    }

    fun resetPosition() {
        currX = beforeScreenX
    }

    fun canChangeWordBehindBee() = if (BeeSprite.STARTING_X > currX + WIDTH && canChangeWord) {
        canChangeWord = false
        true
    } else false

    fun createTopRect(): Rect =
            createRect(currX.toInt(), topPipeShiftY().toInt(), WIDTH, HEIGHT)

    fun createMidRect() =
            createRect(currX.toInt(), midPipeShiftY().toInt(), WIDTH, MID_HEIGHT)

    fun createBotRect() =
            createRect(currX.toInt(), botPipeShiftY().toInt(), WIDTH, HEIGHT)

    private fun createRect(pipeX: Int, pipeY: Int, pipeWidth: Int, pipeHeight: Int) =
            Rect(pipeX, pipeY, pipeX + pipeWidth, pipeY + pipeHeight)
}
