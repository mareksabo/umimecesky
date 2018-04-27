package cz.muni.fi.umimecesky.flappygame.sprite

import android.app.Activity
import android.graphics.Canvas
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayHeight
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayWidth
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * @author Marek Sabo
 */
class FillWordSprite : Sprite {

    companion object {
        private val paint = GraphicsHelper.createThickWordPaint()
    }

    var text: String = ""

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, displayWidth() / 2f, displayHeight() / 10f, paint)
    }

    override fun move() {}
    override fun reset() {}
    override fun intro(activity: Activity): FancyShowCaseView? = null

}