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

package cz.muni.fi.umimecesky.game.ball.hole

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.Dimensions

/**
 * @author Marek Sabo
 */
@SuppressLint("ViewConstructor")
class HoleView(hole: Hole, context: Context?) : View(context) {

    val textViewInside: TextView


    init {
        setBackgroundResource(R.drawable.hole)
        translationX = hole.middle.x
        translationY = hole.middle.y

        val textInside = if (hole is ResultHole) hole.result else ""
        textViewInside = createTextInside(textInside)
    }

    private fun createTextInside(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.textSize = Dimensions.defaultTextSize
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER

        textView.translationX = translationX
        textView.translationY = translationY
        return textView
    }
}