package cz.muni.fi.umimecesky.utils

import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.prefs
import java.util.*

/**
 * Represents static methods which works with data from umimecesky webpage.

 * @see [https://www.umimecesky.cz](https://www.umimecesky.cz)
 */
object WebUtil {

    /**
     * Gets categories with saved player's level state.
     * Categories are bundled into testable groups, taken from original website (first 6 categories):
     *
     * @see [https://www.umimecesky.cz/roboti](https://www.umimecesky.cz/roboti)
     */
    @Suppress("SENSELESS_COMPARISON")
    fun getWebConcepts(): MutableList<RaceConcept> {
        val concepts: MutableList<RaceConcept>

        val jsonConcept = prefs.maxRobotsCategories

        // TODO: name = null because of obfuscator
        if (jsonConcept == null || (jsonConcept.any { it.name == null })) {
            concepts = initWebConcepts()
            prefs.maxRobotsCategories = concepts
        } else {
            concepts = jsonConcept.toMutableList()
        }
        concepts.sort()
        return concepts
    }

    fun updateConcept(concept: RaceConcept) {
        val list = getWebConcepts()
        val index = list.indexOf(concept)
        list[index] = concept
        prefs.maxRobotsCategories = list
    }

    private fun initWebConcepts(): MutableList<RaceConcept> = listOf(
            RaceConcept("Vyjmenovaná slova",
                    Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                    7),

            RaceConcept("Koncovky Y/I",
                    Arrays.asList(8, 9, 10, 11, 12, 13, 14),
                    7),

            RaceConcept("Psaní ě",
                    Arrays.asList(15, 16, 17, 18),
                    6),

            RaceConcept("Zdvojené hlásky",
                    Arrays.asList(19, 20, 21),
                    5),

            RaceConcept("Párové hlásky",
                    Arrays.asList(22, 23, 24, 25),
                    5),

            RaceConcept("Přejatá slova, délka samohlásek",
                    Arrays.asList(26, 27, 28, 29),
                    7),

            RaceConcept("Skloňování",
                    Arrays.asList(30, 31, 32, 33),
                    5),

            RaceConcept("Zkratky a typografie",
                    Arrays.asList(34, 35, 36, 37),
                    5),

            RaceConcept("Velká písmena – lidé, skupiny, organizace, čas",
                    Arrays.asList(38, 39, 40, 41),
                    7),

            RaceConcept("Velká písmena – místa",
                    Arrays.asList(42, 43, 44, 45),
                    5)
    ).toMutableList()
}
