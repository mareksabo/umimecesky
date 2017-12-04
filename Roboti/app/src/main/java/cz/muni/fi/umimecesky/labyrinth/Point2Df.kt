package cz.muni.fi.umimecesky.labyrinth

/**
 * @author Marek Sabo
 */
class Point2Df(var x : Float, var y : Float) {
    constructor(point2Df: Point2Df) : this(point2Df.x, point2Df.y)

    override fun toString(): String = "[$x, $y]"

    fun setTo(point2Df: Point2Df) {
        this.x = point2Df.x
        this.y = point2Df.y
    }
}