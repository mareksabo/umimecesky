package cz.muni.fi.umimecesky.labyrinth.hole

import cz.muni.fi.umimecesky.labyrinth.Circle
import cz.muni.fi.umimecesky.labyrinth.Dimensions.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Point2Df

/**
 * @author Marek Sabo
 */
open class Hole(middlePoint: Point2Df) : Circle(middlePoint, holeRadius) {

    override fun toString(): String = "Hole [$middle.x, $middle.y]"

}