package cz.muni.fi.umimecesky.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.umimecesky.pojo.RaceConcept;

import static cz.muni.fi.umimecesky.utils.Conversion.conceptToNames;

/**
 * Represents static methods which works with data from umimecesky webpage.
 *
 * @see <a href="https://www.umimecesky.cz">https://www.umimecesky.cz</a>
 */

public class WebUtil {
    private final static String JSON_CONCEPTS = "jsonConcepts";

    /**
     * Gets categories with saved player's level state.
     * Categories are bundled into testable groups, taken from original website (first 6 categories):
     *
     * @see <a href="https://www.umimecesky.cz/roboti">https://www.umimecesky.cz/roboti</a>
     */
    public static List<RaceConcept> getWebConcepts(Context context) {
        List<RaceConcept> concepts;

        SharedPreferences sharedPreferences = Util.getSharedPreferences(context);
        String jsonConcept = sharedPreferences.getString(JSON_CONCEPTS, null);

        if (jsonConcept != null) {
            concepts = new Gson().fromJson(jsonConcept, new TypeToken<List<RaceConcept>>() {
            }.getType());
            if (concepts.size() != initWebConcepts().size()) {
                concepts = addMissingConcepts(concepts);
            }
        } else {
            concepts = initWebConcepts();
            setWebConcepts(context, concepts);
        }
        Collections.sort(concepts);
        return concepts;
    }

    private static List<RaceConcept> addMissingConcepts(List<RaceConcept> oldConcepts) {
        List<String> oldConceptNames = conceptToNames(oldConcepts);
        for (RaceConcept raceConcept : initWebConcepts()) {
            if (!oldConceptNames.contains(raceConcept.getName())) {
                oldConcepts.add(raceConcept);
            }
        }
        return oldConcepts;
    }


    public static void updateConcept(Context context, RaceConcept concept) {
        List<RaceConcept> list = getWebConcepts(context);
        int index = list.indexOf(concept);
        list.set(index, concept);
        setWebConcepts(context, list);
    }

    private static void setWebConcepts(Context context, List<RaceConcept> concepts) {
        SharedPreferences sharedPreferences = Util.getSharedPreferences(context);
        String json = new Gson().toJson(concepts);
        sharedPreferences.edit().putString(JSON_CONCEPTS, json).apply();
    }

    public static List<RaceConcept> initWebConcepts() {
        List<RaceConcept> concepts = new ArrayList<>();

        concepts.add(
                new RaceConcept("Vyjmenovaná slova",
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                        7)
        );
        concepts.add(
                new RaceConcept("Koncovky Y/I",
                        Arrays.asList(8, 9, 10, 11, 12, 13, 14),
                        7)
        );
        concepts.add(
                new RaceConcept("Psaní ě",
                        Arrays.asList(15, 16, 17, 18),
                        6)
        );
        concepts.add(
                new RaceConcept("Zdvojené hlásky",
                        Arrays.asList(19, 20, 21),
                        5)
        );
        concepts.add(
                new RaceConcept("Párové hlásky",
                        Arrays.asList(22, 23, 24, 25),
                        5)
        );
        concepts.add(
                new RaceConcept("Přejatá slova, délka samohlásek",
                        Arrays.asList(26, 27, 28, 29),
                        7)
        );
        concepts.add(
                new RaceConcept("Skloňování",
                        Arrays.asList(30, 31, 32, 33),
                        5)
        );
        concepts.add(
                new RaceConcept("Zkratky a typografie",
                        Arrays.asList(34, 35, 36, 37),
                        5)
        );
        concepts.add(
                new RaceConcept("Velká písmena",
                        Arrays.asList(38, 39, 40, 41, 42, 43, 44, 45),
                        7)
        );

        return concepts;
    }
}
