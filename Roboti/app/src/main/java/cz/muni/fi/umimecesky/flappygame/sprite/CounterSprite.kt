package cz.muni.fi.umimecesky.flappygame.sprite

import android.graphics.Canvas
import cz.muni.fi.umimecesky.ballgame.Dimensions
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.prefs

/**
 * @author Marek Sabo
 */
class CounterSprite(private val raceConcept: RaceConcept) {

    companion object {
        private val paint = GraphicsHelper.createThickWordPaint()
    }

    private var currentCount = 0
        set(value) {
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

    private var text: String = ""

    private fun updateText() {
        text = "$currentCount / $maxCount"
    }

    fun increaseCounter() = currentCount++
    fun resetCounter() {
        currentCount = 0
    }

    fun draw(canvas: Canvas) {
        canvas.drawText(text, Dimensions.displayWidth() / 10f, Dimensions.displayHeight() / 10f, paint)
    }

}