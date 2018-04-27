package cz.muni.fi.umimecesky.flappygame.sprite

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Rect
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
        private val X = displayWidth() / 2f
        private val Y = displayHeight() / 10f
    }

    var text: String = ""

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, X, Y, paint)
    }

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