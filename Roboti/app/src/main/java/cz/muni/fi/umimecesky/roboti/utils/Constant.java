package cz.muni.fi.umimecesky.roboti.utils;

import android.graphics.Color;

public final class Constant {

    public static final int TRAINING_NEW_WORD_DELAY = 800; //ms
    public static final int RACE_NEW_WORD_DELAY = 200; //ms

    public static final int RAW_HOPS_TO_WIN = 5;
    public static final int STROKE_WIDTH = 8;

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int CORRECT_COLOR = Color.parseColor("#4C924C");
    public static final int WRONG_COLOR = Color.RED;

    public static final String LAST_FILLED_WORD = "lastWord";
    public static final String IS_FILLED = "isFilled";
    public static final String TICKED_CATEGORIES_EXTRA = "ticked_categories";
    public static final String RACE_CONCEPT_EXTRA = "concept";
    static final String SHARED_PREFS_FILE = "shared_prefs";

}
