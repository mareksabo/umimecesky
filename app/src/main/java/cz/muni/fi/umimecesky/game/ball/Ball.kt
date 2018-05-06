/*
 * Copyright (c) 2018 Marek Sabo
 * Modifications copyright (C) 2010 The Android Open Source Project
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

package cz.muni.fi.umimecesky.game.ball

import android.content.Context
import android.view.View
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeRadius
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeSize
import cz.muni.fi.umimecesky.game.ball.Dimensions.maxBallPosition
import cz.muni.fi.umimecesky.game.ball.Dimensions.metersToPixelsX
import cz.muni.fi.umimecesky.game.ball.Dimensions.metersToPixelsY
import cz.muni.fi.umimecesky.game.ball.hole.Hole
import cz.muni.fi.umimecesky.prefs

/**
 * @author Marek Sabo
 */
class Ball(context: Context) : View(context) {

    private val maxBallPosition = maxBallPosition()

    companion object {
        private val zeroVelocity = Point2Df(0f, 0f)
    }

    private val dT = 0.003f * (2 + prefs.ballWeight)

    val initialPosition = Point2Df(maxBallPosition.x / 2, maxBallPosition.y / 2)

    private var velocity = Point2Df(zeroVelocity)

    /*
     * Position cannot be outside display, when touching an edge change velocity to 0.
     */
    private var posX = initialPosition.x
        set(value) {
            field = value
            if (value >= maxBallPosition.x) {
                field = maxBallPosition.x
                velocity.x = 0f
            } else if (value <= 0f) {
                field = 0f
                velocity.x = 0f
            }

            translationX = field
        }

    private var posY = initialPosition.y
        set(value) {
            field = value
            if (value >= maxBallPosition.y) {
                field = maxBallPosition.y
                velocity.y = 0f
            } else if (posY <= 0f) {
                field = 0f
                velocity.y = 0f
            }

            translationY = field
        }

    init {
        setBackgroundResource(R.drawable.ball)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        posX = initialPosition.x
        posY = initialPosition.y
    }

    fun computeMove(sx: Float, sy: Float) {

        val ax = -sx / 8
        val ay = -sy / 8

        posX += metersToPixelsX() * (velocity.x * dT + ax * dT * dT / 2)
        posY -= metersToPixelsY() * (velocity.y * dT + ay * dT * dT / 2)

        velocity.x += ax * dT
        velocity.y += ay * dT
    }

    fun recreateBall(actionAfter: () -> Unit) {
        synchronized(this) {
            setInitialAttributes()
            val animator = animate()
            animator.withStartAction { visibility = View.VISIBLE }
                    .withEndAction { actionAfter() }
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .duration = 600L
            animator.start()
        }
    }

    private fun setInitialAttributes() {
        posX = initialPosition.x
        posY = initialPosition.y

        stop()
    }

    fun checkInside(hole: Hole): Boolean = hole.middle.distance(posX, posY) <= holeRadius

    fun checkTouching(hole: Hole): Boolean = hole.middle.distance(posX, posY) <= holeSize

    private fun stop() = velocity.setTo(zeroVelocity)

    fun reverseVelocity() {
        velocity.x *= -1
        velocity.y *= -1
    }

    override fun toString(): String = "Ball [$posX, $posY]"


}
