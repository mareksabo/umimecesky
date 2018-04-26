package cz.muni.fi.umimecesky.flappygame.sprite

import android.graphics.Canvas
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayHeight
import cz.muni.fi.umimecesky.ballgame.Dimensions.displayWidth
import cz.muni.fi.umimecesky.flappygame.GraphicsHelper

/**
 * @author Marek Sabo
 */
class FillWordSprite {

    companion object {
        private val paint = GraphicsHelper.createThickWordPaint()
    }

    var text: String = ""

    fun draw(canvas: Canvas) {
        canvas.drawText(text, displayWidth() / 2f, displayHeight() / 10f, paint)
    }

}