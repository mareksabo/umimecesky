package cz.muni.fi.umimecesky.labyrinth.hole

/**
 * @author Marek Sabo
 */
open class Hole(val circle: HoleCircle) {

    fun middle() = circle.middle

    override fun toString(): String = "Hole [$circle.middle.x, $circle.middle.y]"

}