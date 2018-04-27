package cz.muni.fi.umimecesky.flappygame

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import cz.muni.fi.umimecesky.flappygame.sprite.BeeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.CounterSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FillWordSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FlowerSprite
import cz.muni.fi.umimecesky.flappygame.sprite.PipeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.Sprite
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.RaceConcept
import java.util.*
import kotlin.concurrent.schedule


/**
 * @author Marek Sabo
 */
@SuppressLint("ViewConstructor")
class GameLogic(private val activity: Activity, private val raceConcept: RaceConcept)
    : SurfaceView(activity), SurfaceHolder.Callback {
    private val thread: Thread

    private lateinit var beeSprite: BeeSprite
    private lateinit var pipe: PipeSprite
    private lateinit var textSprite: FillWordSprite
    private lateinit var counterSprite: CounterSprite
    private lateinit var sprites: List<Sprite>

    private val wordGenerator = WordGenerator(context, raceConcept)
    private var incorrectAnswer = false

    private var currentWord: FillWord = FillWord.EMPTY_WORD
        set(value) {
            field = value
            textSprite.text = value.wordMissing
            Timer().schedule(300) {
                pipe.answers = RandomAnswers(currentWord)
            }
        }

    private val movementSimulator: MovementSimulator

    init {
        holder.addCallback(this)
        movementSimulator = MovementSimulator(holder, this)
        thread = Thread(movementSimulator)
        isFocusable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        beeSprite.doJump()
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        sprites = createSprites()
        currentWord = wordGenerator.getNextWord()
        movementSimulator.running = true
        val canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        GameIntroduce(activity, sprites) {
            thread.start()
        }

    }

    private fun createSprites(): List<Sprite> {
        val flowerSprite = FlowerSprite(resources)
        pipe = PipeSprite(resources)
        textSprite = FillWordSprite()
        counterSprite = CounterSprite(raceConcept)
        beeSprite = BeeSprite(resources)
        return listOf(flowerSprite, pipe, textSprite, counterSprite, beeSprite)
//        resetLevel()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        movementSimulator.running = false
        while (true) {
            try {
                thread.join()
                break
            } catch (e: InterruptedException) {
            }
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            super.draw(canvas)
            canvas.drawRGB(140, 195, 255)
            sprites.forEach { it.draw(canvas) }
        }
    }

    fun update() {
        if (beeSprite.isOutsideScreen() || isInCollision(beeSprite, pipe)) resetLevel()
        if (isInWrongAnswer()) {
            pipe.markIncorrectAnswerRed()
            incorrectAnswer = true
        }

        sprites.forEach { it.move() }

        if (pipe.canChangeWordBehindBee()) {
            if (incorrectAnswer) {
                resetLevel()
            } else {
                currentWord = wordGenerator.getNextWord()
                counterSprite.increaseCounter()
            }
        }
    }

    private fun resetLevel() {
        Thread.sleep(500L)
        sprites.forEach { it.reset() }
        incorrectAnswer = false
    }

    private fun isInCollision(beeSprite: BeeSprite, pipeSprite: PipeSprite): Boolean {
        val beeBounds = beeSprite.createRect()

        return beeBounds.intersect(pipeSprite.createTopRect()) ||
                beeBounds.intersect(pipeSprite.createMidRect()) ||
                beeBounds.intersect(pipeSprite.createBotRect())
    }

    private fun isInWrongAnswer() = beeSprite.createRect().intersect(pipe.createWrongAnswerRect())

}
