package cz.muni.fi.umimecesky.flappygame


import android.graphics.Canvas
import android.view.SurfaceHolder

/**
 * @author Marek Sabo
 */
class MovementSimulator(private val surfaceHolder: SurfaceHolder, private val gameLogic: GameLogic) : Runnable {

    companion object {
        private const val TARGET_FPS = 40
        private const val TARGET_TIME = 1000L / TARGET_FPS
        private var canvas: Canvas? = null
    }

    @Volatile
    var running: Boolean = true

    override fun run() {
        while (running) {
            val startTime = System.nanoTime()

            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameLogic.update()
                    gameLogic.draw(canvas)
                }
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
            }

            val timeMillis = (System.nanoTime() - startTime) / 1_000_000
            val waitTime = TARGET_TIME - timeMillis
            if (waitTime > 0) Thread.sleep(waitTime)
        }

    }
}
