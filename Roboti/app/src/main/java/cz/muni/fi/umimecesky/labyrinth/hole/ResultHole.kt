package cz.muni.fi.umimecesky.labyrinth.hole

/**
 * @author Marek Sabo
 */
abstract class ResultHole(circle: HoleCircle, val result: String)
    : Hole(circle) { // todo: correct ctor

    abstract fun isCorrect() : Boolean

}