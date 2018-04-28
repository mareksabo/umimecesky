package cz.muni.fi.umimecesky.enums

/**
 * @author Marek Sabo
 */
sealed class Difficulty(val name: String) {
    object Easy : Difficulty("Lehké")
    object Medium : Difficulty("Težší")
    object Hard : Difficulty("Náročné")
    object Unknown : Difficulty("")

    companion object {
        val difficulties = arrayOf(Easy, Medium, Hard)
        val difficultyNames = difficulties.map { it.name }
    }

    override fun toString() = name
}

fun String.toDifficulty(): Difficulty {
    val index = Difficulty.difficultyNames.indexOf(this)
    return if (index >= 0) Difficulty.difficulties[index] else Difficulty.Unknown
}

