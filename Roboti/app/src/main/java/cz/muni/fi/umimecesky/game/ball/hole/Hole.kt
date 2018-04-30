package cz.muni.fi.umimecesky.game.ball.hole

import cz.muni.fi.umimecesky.game.ball.Circle
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeRadius
import cz.muni.fi.umimecesky.game.ball.Point2Df

/**
 * @author Marek Sabo
 */
open class Hole(middlePoint: Point2Df) : Circle(middlePoint, holeRadius) {

    override fun toString(): String = "Hole [$middle.x, $middle.y]"

}