package cz.muni.fi.umimecesky.labyrinth.hole

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.labyrinth.Dimensions

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