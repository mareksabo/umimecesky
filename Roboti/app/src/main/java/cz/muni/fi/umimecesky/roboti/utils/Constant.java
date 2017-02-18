package cz.muni.fi.umimecesky.roboti.utils;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constant {

    public static final int TRAINING_NEW_WORD_DELAY = 800; //ms
    public static final int RACE_NEW_WORD_DELAY = 300; //ms
    public static final float ROBOT_MOVE = 80.0f;
    public static final float WINNING_X_COORDINATES = 800 - ROBOT_MOVE;

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DARK_GREEN = Color.parseColor("#4C924C");

    public static final String LAST_FILLED_WORD = "lastWord";
    public static final String TICKED_CATEGORIES = "TICKED_CATEGORIES";
    public static final String IS_FILLED = "isFilled";
    static final String SHARED_PREFS_FILE = "shared_prefs";

    /**
     * Categories bundled into testable groups according to original website (first 6 categories).
     * @see <a href="https://www.umimecesky.cz/roboti">https://www.umimecesky.cz/roboti</a>
     */
    public static final Map<String, List<Integer>> CONCEPT_NAME_TO_ID;

    static {
        Map<String, List<Integer>> categoryIds = new HashMap<>();

        categoryIds.put("Koncovky Y/I",
                Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        categoryIds.put("Vyjmenovaná slova",
                Arrays.asList(8, 9, 10, 11, 12, 13, 14));
        categoryIds.put("Přejatá slova, délka samohlásek",
                Arrays.asList(38, 39, 40, 41));
        categoryIds.put("Psaní ě",
                Arrays.asList(26, 27, 28, 29));
        categoryIds.put("Zkratky a typografie",
                Arrays.asList(15, 16, 17, 18));
        categoryIds.put("Velká písmena",
                Arrays.asList(34, 35, 36, 37));

        CONCEPT_NAME_TO_ID = Collections.unmodifiableMap(categoryIds);
    }

}
