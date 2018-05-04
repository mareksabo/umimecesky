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
import cz.muni.fi.umimecesky.game.flappy.GraphicsHelper
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * @author Marek Sabo
 */
class CounterSprite(private val raceConcept: RaceConcept) : Sprite {

    companion object {
        private val paint = GraphicsHelper.createThickWordPaint()
        private val X = Dimensions.displayWidth() / 10f
        private val Y = Dimensions.displayHeight() / 10f
    }

    var currentCount = 0
        private set(value) {
            field = value
            updateText()
            if (maxCount < value) maxCount = value
        }

    private var maxCount = prefs.getBestScore(raceConcept)
        set(value) {
            field = value
            updateText()
            prefs.setBestScore(raceConcept, value)
        }

    private var text: String = "$currentCount / $maxCount"

    private fun updateText() {
        text = "$currentCount / $maxCount"
    }

    fun increaseCounter() = currentCount++

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, X, Y, paint)
    }

    override fun move() {}

    override fun reset() {
        currentCount = 0
    }

    override fun intro(activity: Activity): FancyShowCaseView? = null


}