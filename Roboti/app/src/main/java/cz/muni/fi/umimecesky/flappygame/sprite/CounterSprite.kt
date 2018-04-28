package cz.muni.fi.umimecesky.flappygame.sprite

import android.app.Activity
import android.graphics.Canvas
import cz.muni.fi.umimecesky.ballgame.Dimensions
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper
import cz.muni.fi.umimecesky.pojo.RaceConcept
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