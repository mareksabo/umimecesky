package cz.muni.fi.umimecesky.ballgame.hole

import cz.muni.fi.umimecesky.ballgame.Circle
import cz.muni.fi.umimecesky.ballgame.Dimensions.holeRadius
import cz.muni.fi.umimecesky.ballgame.Point2Df

/**
 * @author Marek Sabo
 */
open class Hole(middlePoint: Point2Df) : Circle(middlePoint, holeRadius) {

    override fun toString(): String = "Hole [$middle.x, $middle.y]"

}