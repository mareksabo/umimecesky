package cz.muni.fi.umimecesky.db;

import static cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.IS_VISIBLE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_FILLED;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_MISSING;

final class DbContract {

    // To prevent someone from accidentally instantiating the contract class, make the constructor private.
    private DbContract() {
    }

    static final String DATABASE_NAME = "Robots.db";
    // If you change the database schema, you must increment the database version.
    static final int DATABASE_VERSION = 1;
    static final String COMMA_SEP = ",";

    //table names
    static final String WORD_TABLE = "fill_words";
    static final String CATEGORY_TABLE = "categories";
    static final String JOIN_TABLE = "words_categories";

    static final String VISIBLE_TRUE = "1";
    static final String VISIBLE_FALSE = "0";

    static final String ALL_WORD_COLUMNS =
            WORD_ID + COMMA_SEP + " " +
            WORD_MISSING + COMMA_SEP + " " +
            WORD_FILLED + COMMA_SEP + " " +
            VARIANT1 + COMMA_SEP + " " +
            VARIANT2 + COMMA_SEP + " " +
            CORRECT_VARIANT + COMMA_SEP + " " +
            EXPLANATION + COMMA_SEP + " " +
            GRADE + COMMA_SEP + " " +
            IS_VISIBLE;


    static class WordColumn {
        static final String WORD_ID = "word_id";
        static final String WORD_MISSING = "missing_word";
        static final String WORD_FILLED = "filled_word";
        static final String VARIANT1 = "first_variant";
        static final String VARIANT2 = "second_variant";
        static final String CORRECT_VARIANT = "correct_variant";
        static final String EXPLANATION = "explanation";
        static final String GRADE = "grade";
        static final String IS_VISIBLE = "is_visible";
    }

    static class CategoryColumn {
        static final String CATEGORY_ID = "category_id";
        static final String NAME = "name";
    }

    static class JoinColumn {
        static final String JOIN_WORD_ID = "join_" + WORD_ID;
        static final String JOIN_CATEGORY_ID = "join_" + CATEGORY_ID;
    }
}

