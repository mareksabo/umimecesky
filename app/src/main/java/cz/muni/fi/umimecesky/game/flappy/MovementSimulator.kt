/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
