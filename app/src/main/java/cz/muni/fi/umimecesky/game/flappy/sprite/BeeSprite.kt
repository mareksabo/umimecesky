package cz.muni.fi.umimecesky.game.flappy.sprite

import android.app.Activity
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.Dimensions.displayHeight
import cz.muni.fi.umimecesky.game.flappy.GraphicsHelper
import me.toptas.fancyshowcase.FancyShowCaseView

/**
 * Bee has its X position same all the time, only Y is changing.
 *
 * @author Marek Sabo
 */
class BeeSprite(resources: Resources) : Sprite {

    companion object {
        private const val WIDTH = 150
        private const val HEIGHT = 130
        private const val MAX_VELOCITY = 6

        const val STARTING_X = 100f
        private const val STARTING_Y = 300f
    }

    private val image = GraphicsHelper.generateImage(resources, R.drawable.bee, WIDTH, HEIGHT)

    private var currY: Float = STARTING_Y
    private var velocityY: Int = MAX_VELOCITY
        set(value) = if (value <= MAX_VELOCITY) field = value else {}

    override fun draw(canvas: Canvas) = canvas.drawBitmap(image, STARTING_X, currY, null)

    override fun move() {
        currY += velocityY
        velocityY += 2
    }

    override fun reset() {
        currY = STARTING_Y
    }

    fun doJump() {
        velocityY = -20
        currY -= defaultMoveX
    }

    fun isOutsideScreen() = displayHeight() < currY || currY < -HEIGHT

    fun createRect(): Rect {
        val beeX = STARTING_X.toInt()
        val beeY = currY.toInt()

        return Rect(beeX, beeY, beeX + WIDTH, beeY + HEIGHT)
    }

    override fun intro(activity: Activity): FancyShowCaseView = FancyShowCaseView.Builder(activity)
            .focusCircleAtPosition((STARTING_X + WIDTH / 2).toInt(),
                    (STARTING_Y + HEIGHT / 2).toInt(),
                    WIDTH / 2 + 10)
            .title("Včela se ovládá dotykem obrazovky")
            .build()
}


