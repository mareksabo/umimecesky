package cz.muni.fi.umimecesky.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.utils.Conversion.conceptToNames
import java.util.*

/**
 * Represents static methods which works with data from umimecesky webpage.

 * @see [https://www.umimecesky.cz](https://www.umimecesky.cz)
 */

object WebUtil {
    private val JSON_CONCEPTS = "jsonConcepts"

    /**
     * Gets categories with saved player's level state.
     * Categories are bundled into testable groups, taken from original website (first 6 categories):

     * @see [https://www.umimecesky.cz/roboti](https://www.umimecesky.cz/roboti)
     */
    fun getWebConcepts(context: Context): MutableList<RaceConcept> {
        var concepts: MutableList<RaceConcept>

        val sharedPreferences = Util.getSharedPreferences(context)
        val jsonConcept = sharedPreferences.getString(JSON_CONCEPTS, null)

        if (jsonConcept != null) {
            concepts = Gson().fromJson<List<RaceConcept>>(jsonConcept, object : TypeToken<List<RaceConcept>>() {
            }.type) as MutableList<RaceConcept>
            if (concepts.size != initWebConcepts().size) {
                concepts = addMissingConcepts(concepts)
            }
        } else {
            concepts = initWebConcepts()
            setWebConcepts(context, concepts)
        }
        Collections.sort(concepts)
        return concepts
    }

    private fun addMissingConcepts(oldConcepts: MutableList<RaceConcept>): MutableList<RaceConcept> {
        val oldConceptNames = conceptToNames(oldConcepts)
        initWebConcepts().filterNotTo(oldConcepts) { oldConceptNames.contains(it.name) }

        for (raceConcept in oldConcepts) {
            if (raceConcept.name == "Velká písmena") {
                oldConcepts.remove(raceConcept)
                break
            }
        }
        return oldConcepts
    }


    fun updateConcept(context: Context, concept: RaceConcept) {
        val list = getWebConcepts(context)
        val index = list.indexOf(concept)
        list[index] = concept
        setWebConcepts(context, list)
    }

    private fun setWebConcepts(context: Context, concepts: List<RaceConcept>) {
        val sharedPreferences = Util.getSharedPreferences(context)
        val json = Gson().toJson(concepts)
        sharedPreferences.edit().putString(JSON_CONCEPTS, json).apply()
    }

    fun initWebConcepts(): MutableList<RaceConcept> {
        val concepts = ArrayList<RaceConcept>()

        concepts.add(
                RaceConcept("Vyjmenovaná slova",
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                        7)
        )
        concepts.add(
                RaceConcept("Koncovky Y/I",
                        Arrays.asList(8, 9, 10, 11, 12, 13, 14),
                        7)
        )
        concepts.add(
                RaceConcept("Psaní ě",
                        Arrays.asList(15, 16, 17, 18),
                        6)
        )
        concepts.add(
                RaceConcept("Zdvojené hlásky",
                        Arrays.asList(19, 20, 21),
                        5)
        )
        concepts.add(
                RaceConcept("Párové hlásky",
                        Arrays.asList(22, 23, 24, 25),
                        5)
        )
        concepts.add(
                RaceConcept("Přejatá slova, délka samohlásek",
                        Arrays.asList(26, 27, 28, 29),
                        7)
        )
        concepts.add(
                RaceConcept("Skloňování",
                        Arrays.asList(30, 31, 32, 33),
                        5)
        )
        concepts.add(
                RaceConcept("Zkratky a typografie",
                        Arrays.asList(34, 35, 36, 37),
                        5)
        )
        concepts.add(
                RaceConcept("Velká písmena – lidé, skupiny, organizace, čas",
                        Arrays.asList(38, 39, 40, 41),
                        7)
        )
        concepts.add(
                RaceConcept("Velká písmena – místa",
                        Arrays.asList(42, 43, 44, 45),
                        5)
        )

        return concepts
    }
}
