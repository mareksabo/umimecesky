package cz.muni.fi.umimecesky.labyrinth.hole

import cz.muni.fi.umimecesky.labyrinth.Circle
import cz.muni.fi.umimecesky.labyrinth.Dimensions.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Point2Df

/**
 * @author Marek Sabo
 */
class HoleCircle(middle: Point2Df) : Circle(middle, holeRadius)