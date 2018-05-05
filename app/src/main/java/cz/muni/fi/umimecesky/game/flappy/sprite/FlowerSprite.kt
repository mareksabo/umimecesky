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

package cz.muni.fi.umimecesky.game.flappy.sprite

import android.app.Activity
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayHeight
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayWidth
import cz.muni.fi.umimecesky.game.flappy.GraphicsHelper
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * Flower has fixed Y value, X is moving towards the users' sprite.
 *
 * @author Marek Sabo
 */
class FlowerSprite(resources: Resources) : Sprite {

    companion object {
        private const val WIDTH = 240
        private const val HEIGHT = 415
        private val beforeScreenX = displayWidth().toFloat()
        private val startingX = beforeScreenX / 3
        private val STARTING_Y = displayHeight() - HEIGHT.toFloat()
        private val DISTANCE_THRESHOLD = displayWidth() / 6
    }

    private val image = GraphicsHelper.generateImage(resources, R.drawable.flower, WIDTH, HEIGHT)
    private var isCloseToPipe = false

    private var currX: Float = startingX
        set(value) {
            field = value
            if (value < -WIDTH) {
                field = beforeScreenX
                checkPipeDistance()
            }
        }

    override fun draw(canvas: Canvas) = canvas.drawBitmap(image, currX, STARTING_Y, null)

    override fun move() {
        currX -= defaultMoveX // move closer to left
    }

    override fun reset() {
        currX = startingX
    }

    override fun intro(activity: Activity): FancyShowCaseView? = null

    private fun checkPipeDistance() {
        if (isCloseToPipe) {
            currX += DISTANCE_THRESHOLD + 2 * WIDTH
            isCloseToPipe = false
        }
    }

    fun setFurtherFromPipe() {
        isCloseToPipe = true
    }

    fun createRect(): Rect {
        val x = currX.toInt()
        val y = STARTING_Y.toInt()
        val biggerWidth = DISTANCE_THRESHOLD
        return Rect(x, y, x + WIDTH + biggerWidth, y + HEIGHT)
    }
}
