package cz.muni.fi.umimecesky.flappygame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import cz.muni.fi.umimecesky.db.helper.wordOpenHelper
import cz.muni.fi.umimecesky.flappygame.sprite.BeeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FillWordSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FlowerSprite
import cz.muni.fi.umimecesky.flappygame.sprite.PipeSprite
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.random
import java.util.*
import kotlin.concurrent.schedule


/**
 * @author Marek Sabo
 */
class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: Thread

    private lateinit var beeSprite: BeeSprite
    private lateinit var flowerSprite: FlowerSprite
    private lateinit var pipe: PipeSprite
    private lateinit var textSprite: FillWordSprite

    private var currentWord: FillWord = context.wordOpenHelper.getRandomWord(1) // todo
        set(value) {
            field = value
            textSprite.text = value.wordMissing
            Timer().schedule(300) {
            pipe.answers = if (random.nextBoolean()) Pair(value.variant1, value.variant2)  // todo RandomPair
                           else Pair(value.variant1, value.variant2)
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
        createSprites()
        currentWord = context.wordOpenHelper.getRandomWord(1)
        movementSimulator.running = true
        thread.start()
    }

    private fun createSprites() {
        beeSprite = BeeSprite(resources)
        flowerSprite = FlowerSprite(resources)
        pipe = PipeSprite(resources)
        textSprite = FillWordSprite()
        resetLevel()
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
            flowerSprite.draw(canvas)
            pipe.draw(canvas)
            textSprite.draw(canvas)
            beeSprite.draw(canvas)
        }
    }

    fun update() {
        if (isInCollision(beeSprite, pipe)) resetLevel()
        if (beeSprite.isOutsideScreen()) resetLevel()

        beeSprite.move()
        flowerSprite.move()
        pipe.move()

        if (pipe.canChangeWordBehindBee()) currentWord = context.wordOpenHelper.getRandomWord(1)
    }

    private fun resetLevel() {
        Thread.sleep(500L)
        beeSprite.resetPosition()
        flowerSprite.resetPosition()
        pipe.resetPosition()
    }

    private fun isInCollision(beeSprite: BeeSprite, pipeSprite: PipeSprite): Boolean {
        val beeBounds = beeSprite.createRect()

        return beeBounds.intersect(pipeSprite.createTopRect()) ||
                beeBounds.intersect(pipeSprite.createMidRect()) ||
                beeBounds.intersect(pipeSprite.createBotRect())
    }

}