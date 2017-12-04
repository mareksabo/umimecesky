package cz.muni.fi.umimecesky.labyrinth.hole

/**
 * @author Marek Sabo
 */
class CorrectHole(circle: HoleCircle, result: String)
    : ResultHole(circle, result) {

    override fun isCorrect(): Boolean = true

}