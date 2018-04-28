package cz.muni.fi.umimecesky.enums

/**
 * Represents word difficulty (taken from database - grade).
 * Name must be unique.
 *
 * @author Marek Sabo
 */
sealed class Difficulty(val name: String, val grade: Int) {
    object Easy : Difficulty("Lehké", 1)
    object Medium : Difficulty("Težší", 2)
    object Hard : Difficulty("Náročné", 3)
    object Unknown : Difficulty("", -1)

    companion object {
        val difficulties by lazy { arrayOf(Easy, Medium, Hard) }
        val difficultyNames by lazy { difficulties.map { it.name } }
    }

    override fun toString() = name
}

fun String.toDifficulty(): Difficulty {
    val index = Difficulty.difficultyNames.indexOf(this)
    return if (index >= 0) Difficulty.difficulties[index] else Difficulty.Unknown
}

