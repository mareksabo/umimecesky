package cz.muni.fi.umimecesky.labyrinth.hole

/**
 * @author Marek Sabo
 */
class IncorrectHole(circle: HoleCircle, result: String)
    : ResultHole(circle, result) {
    override fun isCorrect(): Boolean = false
}