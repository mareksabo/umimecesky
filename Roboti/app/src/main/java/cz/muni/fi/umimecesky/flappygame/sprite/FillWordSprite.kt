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