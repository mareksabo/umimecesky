package cz.muni.fi.umimecesky.flappygame

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import cz.muni.fi.umimecesky.flappygame.sprite.BeeSprite
import cz.muni.fi.umimecesky.flappygame.sprite.FlowerSprite
import cz.muni.fi.umimecesky.flappygame.sprite.PipeSprite

val screenHeight by lazy { Resources.getSystem().displayMetrics.heightPixels }
val screenWidth by lazy { Resources.getSystem().displayMetrics.widthPixels }

/**
 * @author Marek Sabo
 */
class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: Thread

    private lateinit var beeSprite: BeeSprite
    private lateinit var flowerSprite: FlowerSprite
    private lateinit var pipe: PipeSprite

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
        movementSimulator.running = true
        thread.start()
    }

    private fun createSprites() {
        beeSprite = BeeSprite(resources)
        flowerSprite = FlowerSprite(resources)
        pipe = PipeSprite(resources)
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
            beeSprite.draw(canvas)
            flowerSprite.draw(canvas)
            pipe.draw(canvas)
        }
    }

    fun update() {
        if (isInCollision(beeSprite, pipe)) resetLevel()
        if (beeSprite.isOutsideScreen()) resetLevel()

        beeSprite.move()
        flowerSprite.move()
        pipe.move()
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
