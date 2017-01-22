package cz.muni.fi.umimecesky.roboti;

import android.provider.BaseColumns;

public final class FillWordContract {

    // To prevent someone from accidentally instantiating the contract class, make the constructor private.
    private FillWordContract() {}

    /* Inner class that defines the table contents */
    public static class WordEntry implements BaseColumns {

        public static final String TABLE_NAME = "fill_words";
        public static final String WORD_MISSING = "missing_word";
        public static final String WORD_FILLED = "filled_word";
        public static final String VARIANT1 = "first_variant";
        public static final String VARIANT2 = "second_variant";
        public static final String CORRECT_VARIANT = "correct_variant";
        public static final String EXPLANATION = "explanation";
        public static final String GRADE = "grade";
        public static final String IS_VISIBLE = "is_visible";
        public static final String VISIBLE_TRUE = "1";
        public static final String VISIBLE_FALSE = "0";
    }
}

