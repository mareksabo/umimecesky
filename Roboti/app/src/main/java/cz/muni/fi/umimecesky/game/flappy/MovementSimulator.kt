package cz.muni.fi.umimecesky.game.flappy


import android.graphics.Canvas
import android.view.SurfaceHolder
import cz.muni.fi.umimecesky.prefs
import kotlin.system.measureTimeMillis

/**
 * @author Marek Sabo
 */
class MovementSimulator(private val surfaceHolder: SurfaceHolder, private val gameLogic: GameLogic) : Runnable {

    private val targetTime = 1000L / prefs.flappyFps

    companion object {
        private var canvas: Canvas? = null
    }

    @Volatile
    var running: Boolean = true

    override fun run() {
        while (running) {
            val timeMs = measureTimeMillis {
                try {
                    canvas = surfaceHolder.lockCanvas()
                    synchronized(surfaceHolder) {
                        gameLogic.update()
                        gameLogic.draw(canvas)
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            val waitTime = targetTime - timeMs
            if (waitTime > 0) Thread.sleep(waitTime)
        }

    }
}
