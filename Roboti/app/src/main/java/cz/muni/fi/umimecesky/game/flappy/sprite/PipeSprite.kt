package cz.muni.fi.umimecesky.game.flappy.sprite

import android.app.Activity
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayHeight
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayWidth
import cz.muni.fi.umimecesky.game.flappy.GraphicsHelper
import cz.muni.fi.umimecesky.game.flappy.SortedAnswers
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.prefs
import me.toptas.fancyshowcase.FancyShowCaseView
import java.util.*

/**
 * @author Marek Sabo
 */
class PipeSprite(resources: Resources) : Sprite {

    private val gapHeight = prefs.flappyGap.size
    private val midHeight = 2 * HEIGHT - gapHeight

    companion object {
        private const val WIDTH = 200
        private val HEIGHT = displayHeight() / 4
        private val beforeScreenX = displayWidth().toFloat()

        private val paint1 = GraphicsHelper.createAnswersPaint()
        private val paint2 = GraphicsHelper.createAnswersPaint()
    }

    var answers: SortedAnswers = SortedAnswers(FillWord.EMPTY_WORD)

    private var canChangeWord = true

    private val topPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_down, WIDTH, HEIGHT)
    private val midPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_mid, WIDTH, midHeight)
    private val botPipe = GraphicsHelper.generateImage(resources, R.drawable.pipe_up, WIDTH, HEIGHT)

    private var shiftY: Float = generateShiftY()

    private var currX: Float = beforeScreenX - WIDTH - 20
        set(value) {
            field = value
            if (value < -WIDTH) this.reset()
        }

    private fun generateShiftY() = Random().nextInt(200) - 100f

    private fun topPipeShiftY() = shiftY - gapHeight / 2
    private fun midPipeShiftY() = topPipeShiftY() + HEIGHT + gapHeight
    private fun botPipeShiftY() = midPipeShiftY() + midHeight + gapHeight

    private fun upperGapMid() = midPipeShiftY() - gapHeight / 2
    private fun lowerGapMid() = botPipeShiftY() - gapHeight / 2

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(topPipe, currX, topPipeShiftY(), null)
        canvas.drawBitmap(midPipe, currX, midPipeShiftY(), null)
        canvas.drawBitmap(botPipe, currX, botPipeShiftY(), null)

        canvas.drawText(answers.first, currX + WIDTH / 2, upperGapMid(), paint1)
        canvas.drawText(answers.second, currX + WIDTH / 2, lowerGapMid(), paint2)
    }

    override fun move() {
        currX -= defaultMoveX // move closer to left
    }

    override fun reset() {
        currX = beforeScreenX
        paint1.color = Color.BLACK
        paint2.color = Color.BLACK
        shiftY = generateShiftY()
        canChangeWord = true
    }

    fun markIncorrectAnswerRed() = if (answers.isFirstCorrect) {
        paint2.color = Color.RED
    } else {
        paint1.color = Color.RED
    }

    fun canChangeWordBehindBee() = if (BeeSprite.STARTING_X > currX + WIDTH && canChangeWord) {
        canChangeWord = false
        true
    } else false

    fun createTopRect(): Rect =
            createRect(currX.toInt(), topPipeShiftY().toInt(), WIDTH, HEIGHT)

    fun createMidRect() =
            createRect(currX.toInt(), midPipeShiftY().toInt(), WIDTH, midHeight)

    fun createBotRect() =
            createRect(currX.toInt(), botPipeShiftY().toInt(), WIDTH, HEIGHT)

    fun createWrongAnswerRect(): Rect {
        val incorrectAnswerY = (if (!answers.isFirstCorrect) upperGapMid() else lowerGapMid()) - gapHeight / 2
        return createRect(currX.toInt(), incorrectAnswerY.toInt(), WIDTH, gapHeight)
    }

    private fun createRect(pipeX: Int, pipeY: Int, pipeWidth: Int, pipeHeight: Int) =
            Rect(pipeX, pipeY, pipeX + pipeWidth, pipeY + pipeHeight)

    override fun intro(activity: Activity): FancyShowCaseView = FancyShowCaseView.Builder(activity)
            .focusRectAtPosition(currX.toInt() + WIDTH / 2,
                    displayHeight() / 2,
                    WIDTH,
                    displayHeight())
            .title("Cílem je vyhýbat se překážkám \n" +
                    "a trefit se do správné mezery")
            .build()
}
