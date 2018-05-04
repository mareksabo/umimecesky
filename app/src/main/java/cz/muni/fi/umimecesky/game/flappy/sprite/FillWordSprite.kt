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
import android.graphics.Rect
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayHeight
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayWidth
import cz.muni.fi.umimecesky.game.flappy.GraphicsHelper
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * @author Marek Sabo
 */
class FillWordSprite : Sprite {

    companion object {
        private val paint = GraphicsHelper.createThickWordPaint()
        private val X = displayWidth() / 2f
        private val Y = displayHeight() / 10f
        private const val MAX_LINE_LETTERS = 28
    }

    var text: String = ""
        set(value) {
            field = value
            breakIndex = value.indexOf(string = " ", startIndex = MAX_LINE_LETTERS)
        }
    private var breakIndex = -1

    override fun draw(canvas: Canvas) {
        if (isMultiLine()) {
            canvas.drawText(text, 0, breakIndex, X, Y, paint)
            canvas.drawText(text, breakIndex, text.length, X, Y + createRect().height(), paint)
        } else canvas.drawText(text, X, Y, paint)
    }

    private fun isMultiLine() = breakIndex != -1

    override fun move() {}
    override fun reset() {}
    override fun intro(activity: Activity): FancyShowCaseView? {
        val rect = createRect()
        return FancyShowCaseView.Builder(activity)
                .focusRectAtPosition(
                        X.toInt(),
                        Y.toInt() - rect.height() / 2,
                        rect.width() + 20,
                        rect.height() + 20)
                .title("Slovo na doplnění je zde")
                .build()
    }

    private fun createRect(): Rect {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds
    }

}