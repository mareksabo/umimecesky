package cz.muni.fi.umimecesky.pojo

import cz.muni.fi.umimecesky.prefs
import java.io.Serializable
import java.util.*

/**
 * Represents concept/category with current state.
 * F.e. pisanie i/y_
 */
@Suppress("DataClassPrivateConstructor")
data class RaceConcept
private constructor(val name: String, val categoryIDs: List<Int>, val numberOfLevels: Int)
    : Serializable, Comparable<RaceConcept> {

    var currentLevel: Int = 1
        private set(value) {
            if (value <= numberOfLevels) field = value
        }

    /**
     * Increases the current level.
     *
     * @return true if level was increased
     */
    fun increaseLevel(): Boolean {
        val oldLevel = currentLevel
        currentLevel += 1
        updatePreferences()
        return oldLevel != currentLevel
    }

    private fun updatePreferences() {
        val list = prefs.maxRobotsCategories
        val index = list.indexOf(this)
        list[index] = this
        prefs.maxRobotsCategories = list
    }

    /**
     * Number describing how far in terms of levels user is.
     * @return  interval higher than 0, lower or equal to 1
     */
    fun levelProgress(): Float = currentLevel / numberOfLevels.toFloat()

    override fun compareTo(other: RaceConcept): Int = this.name.compareTo(other.name)

    override fun toString(): String {
        return "RaceConcept(name='$name', categoryIDs=$categoryIDs, " +
                "numberOfLevels=$numberOfLevels, currentLevel=$currentLevel)"
    }

    companion object {
        val SPECIAL_E = "Psaní ě"
        val CASES = "Skloňování"
        val DOUBLED = "Zdvojené hlásky"
        val SHORTCUTS = "Zkratky a typografie"
        /**
         * Categories are bundled into testable groups, taken from original website.
         * Now sorted according to the most popular.
         *
         * @see [https://www.umimecesky.cz/roboti](https://www.umimecesky.cz/roboti)
         */
        val initConcepts: List<RaceConcept> = listOf(
                RaceConcept("Vyjmenovaná slova",
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                        7),

                RaceConcept("Koncovky Y/I",
                        Arrays.asList(8, 9, 10, 11, 12, 13, 14),
                        7),

                RaceConcept(SPECIAL_E,
                        Arrays.asList(15, 16, 17, 18),
                        6),

                RaceConcept("Párové hlásky",
                        Arrays.asList(22, 23, 24, 25),
                        5),

                RaceConcept("Přejatá slova, délka samohlásek",
                        Arrays.asList(26, 27, 28, 29),
                        7),

                RaceConcept(CASES,
                        Arrays.asList(30, 31, 32, 33),
                        5),

                RaceConcept(SHORTCUTS,
                        Arrays.asList(34, 35, 36),
                        5),

                RaceConcept("Velká písmena – lidé, skupiny, organizace, čas",
                        Arrays.asList(38, 39, 40, 41),
                        7),

                RaceConcept("Velká písmena – místa",
                        Arrays.asList(42, 43, 44, 45),
                        5),

                RaceConcept(DOUBLED,
                        Arrays.asList(19, 20, 21),
                        5)
        )
        val conceptNames = initConcepts.map { it.name }.toTypedArray()
    }
}
