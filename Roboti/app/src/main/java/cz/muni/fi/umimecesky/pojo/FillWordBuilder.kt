package cz.muni.fi.umimecesky.pojo

class FillWordBuilder {
    private var id: Long = 0
    private lateinit var wordMissing: String
    private lateinit var wordFilled: String
    private lateinit var variant1: String
    private lateinit var variant2: String
    private var correctVariant: Int = 0
    private lateinit var explanation: String
    private var grade: Int = 0
    private var visibility: Boolean = false

    fun id(id: Long): FillWordBuilder {
        this.id = id
        return this
    }

    fun wordMissing(wordMissing: String): FillWordBuilder {
        this.wordMissing = wordMissing
        return this
    }

    fun wordFilled(wordFilled: String): FillWordBuilder {
        this.wordFilled = wordFilled
        return this
    }

    fun variant1(variant1: String): FillWordBuilder {
        this.variant1 = variant1
        return this
    }

    fun variant2(variant2: String): FillWordBuilder {
        this.variant2 = variant2
        return this
    }

    fun correctVariant(correctVariant: Int): FillWordBuilder {
        this.correctVariant = correctVariant
        return this
    }

    fun explanation(explanation: String): FillWordBuilder {
        this.explanation = explanation
        return this
    }

    fun grade(grade: Int): FillWordBuilder {
        this.grade = grade
        return this
    }

    fun visibility(visibility: Boolean): FillWordBuilder {
        this.visibility = visibility
        return this
    }

    fun createFillWord(): FillWord =
            FillWord(id, wordMissing, wordFilled, variant1, variant2, correctVariant, explanation, grade, visibility)
}