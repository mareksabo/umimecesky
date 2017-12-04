package cz.muni.fi.umimecesky.labyrinth.hole

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import cz.muni.fi.umimecesky.R

/**
 * @author Marek Sabo
 */
@SuppressLint("ViewConstructor")
class HoleView(hole: Hole, context: Context?) : View(context) {

    val textInside: TextView


    init {
        setBackgroundResource(R.drawable.hole)
        translationX = hole.middle().x
        translationY = hole.middle().y

        if (hole is ResultHole) {
            textInside = createTextInside(hole.result)
            textInside.textSize += 1f
        } else {
            textInside = createTextInside("")
        }
    }

    private fun createTextInside(text: String): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER

        textView.translationX = translationX
        textView.translationY = translationY
        return textView
    }
}