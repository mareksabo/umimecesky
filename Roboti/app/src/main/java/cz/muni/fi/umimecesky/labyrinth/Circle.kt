package cz.muni.fi.umimecesky.labyrinth

import cz.muni.fi.umimecesky.labyrinth.Constant.ballRadius

/**
 * @author Marek Sabo
 */
open class Circle(val middle: Point2Df, private val radius: Int) {

    fun isRelativelyClose(another: Circle): Boolean {
        val distanceBetweenHoles = radius + another.radius + ballRadius
        return isInRadius(another, distanceBetweenHoles)
    }

    fun isTouching(another: Circle): Boolean {
        val distanceBetweenHoles = radius + another.radius
        return isInRadius(another, distanceBetweenHoles)
    }

    private fun isInRadius(another: Circle, distanceBetweenHoles: Int) =
            Math.abs(middle.x - another.middle.x) <= distanceBetweenHoles
                    && Math.abs(middle.y - another.middle.y) <= distanceBetweenHoles
}