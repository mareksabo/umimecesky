package cz.muni.fi.umimecesky.flappygame

import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.random

/**
 * @author Marek Sabo
 */
class RandomAnswers {

    constructor(fillWord: FillWord) :
            this(fillWord.variant1, fillWord.variant2, fillWord.correctVariant)

    private constructor(_first: String, _second: String, correct: Int) {

        if (random.nextBoolean()) {
            first = _second
            second = _first
            isFirstCorrect = correct == 1
        } else {
            first = _first
            second = _second
            isFirstCorrect = correct == 0
        }
    }

    val isFirstCorrect: Boolean

    val first: String
    val second: String

}