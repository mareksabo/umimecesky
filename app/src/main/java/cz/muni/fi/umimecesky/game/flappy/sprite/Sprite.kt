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
import android.graphics.Canvas
import cz.muni.fi.umimecesky.game.ball.Dimensions
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * @author Marek Sabo
 */
interface Sprite {

    val defaultMoveX: Int
        get() = Dimensions.displayWidth() / 200

    /**
     * Draws sprite on canvas.
     */
    fun draw(canvas: Canvas)

    /**
     * Moves the sprite.
     */
    fun move()

    /**
     * Called when level is restarted, gets to the initial state, f.e. it resets the position.
     */
    fun reset()

    /**
     * Does intro if available, otherwise returns null.
     */
    fun intro(activity: Activity): FancyShowCaseView?
}