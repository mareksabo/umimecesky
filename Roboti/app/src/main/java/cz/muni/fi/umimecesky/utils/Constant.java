package cz.muni.fi.umimecesky.utils;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

public final class Constant {

    public static final int TRAINING_NEW_WORD_DELAY_MS = 800;
    public static final int RACE_NEW_WORD_DELAY_MS = 200;

    public static final int RAW_HOPS_TO_WIN = 5;
    public static final int STROKE_WIDTH = 8;

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int CORRECT_COLOR = Color.parseColor("#4C924C");
    public static final int WRONG_COLOR = Color.RED;

    public static final String LAST_FILLED_WORD = "lastWord";
    public static final String IS_FILLED = "isFilled";
    public static final String TICKED_CATEGORIES_EXTRA = "ticked_categories";
    public static final String RACE_CONCEPT_EXTRA = "concept";
    public static final String LAST_SPINNER_VALUE = "last_spinner_value";
    public static final String SHARED_PREFS_FILE = "shared_prefs";

    public static final String INFINITY = "Nekonečno";
    public static final List<String> ROUND_POSSIBILITIES = Arrays.asList( "10", "20", "35", "50", INFINITY);

    public static final int ROBOT_START_DELAY_MS = 200;
}
