package cz.muni.fi.umimecesky.labyrinth

/**
 * @author Marek Sabo
 */
open class Circle(val middle: Point2Df, private val radius: Int) {

    fun isTouching(another: Circle): Boolean {
        return Math.abs(middle.x - another.middle.x) <= radius + 3 * another.radius
                && Math.abs(middle.y - another.middle.y) <= radius + 3 * another.radius
    }

}