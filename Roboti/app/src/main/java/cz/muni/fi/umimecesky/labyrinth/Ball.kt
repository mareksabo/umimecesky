package cz.muni.fi.umimecesky.labyrinth

import android.content.Context
import android.view.View
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.labyrinth.Constant.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Constant.maxBallPosition
import cz.muni.fi.umimecesky.labyrinth.Constant.metersToPixels
import cz.muni.fi.umimecesky.labyrinth.hole.Hole

/**
 * @author Marek Sabo
 */
class Ball(context: Context) : View(context) {

    companion object {

        val initialPosition = Point2Df(
                maxBallPosition.x / 2,
                maxBallPosition.y / 2
        )
        private val initialVelocity = Point2Df(0f, 0f)
        private val dT = 0.015f

    }

    private var velocity = Point2Df(initialVelocity)
    var isInsideHole = false

    /*
     * Position cannot be outside display, when touching an edge change velocity to 0.
     */
    private var posX = initialPosition.x
        set(value) {
            val xMax = maxBallPosition.x
            field = value
            if (value >= xMax) {
                field = xMax
                velocity.x = 0f
            } else if (value <= 0f) {
                field = 0f
                velocity.x = 0f
            }

            translationX = field
        }

    private var posY = initialPosition.y
        set(value) {
            val yMax = maxBallPosition.y
            field = value
            if (value >= yMax) {
                field = yMax
                velocity.y = 0f
            } else if (posY <= 0f) {
                field = 0f
                velocity.y = 0f
            }

            translationY = field
        }

    init {
        setBackgroundResource(R.drawable.ball)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        posX = initialPosition.x
        posY = initialPosition.y
    }

    fun computeMove(sx: Float, sy: Float) {

        val ax = -sx / 5
        val ay = -sy / 5

        posX += metersToPixels.x * (velocity.x * dT + ax * dT * dT / 2)
        posY -= metersToPixels.y * (velocity.y * dT + ay * dT * dT / 2)

        velocity.x += ax * dT
        velocity.y += ay * dT
    }

    fun recreateBall() {
        synchronized(this) {
            setInitialAttributes()
            val animator = animate()
            animator.withStartAction { visibility = View.VISIBLE }
                    .withEndAction { invalidate() }
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
            animator.duration = 600L
            animator.start()
        }
    }

    private fun setInitialAttributes() {
        posX = initialPosition.x
        posY = initialPosition.y

        velocity.setTo(initialVelocity)

        isInsideHole = false
    }

    fun checkInside(hole: Hole): Boolean {
        val isInside = Math.abs(posX - hole.middle().x) <= holeRadius
                && Math.abs(posY - hole.middle().y) <= holeRadius
        return isInside
    }

    override fun toString(): String = "Ball [$posX, $posY]"


}
