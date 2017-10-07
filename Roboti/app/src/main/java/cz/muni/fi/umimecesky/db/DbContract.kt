package cz.muni.fi.umimecesky.db

import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.IS_VISIBLE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_FILLED
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_MISSING

internal object DbContract {

    val DATABASE_NAME = "Robots.db"
    // If you change the database schema, you must increment the database version.
    val DATABASE_VERSION = 1
    val COMMA_SEP = ","

    //table names
    val WORD_TABLE = "fill_words"
    val CATEGORY_TABLE = "categories"
    val JOIN_TABLE = "words_categories"

    val VISIBLE_TRUE = "1"
    val VISIBLE_FALSE = "0"

    val ALL_WORD_COLUMNS =
            WORD_ID + COMMA_SEP + " " +
                    WORD_MISSING + COMMA_SEP + " " +
                    WORD_FILLED + COMMA_SEP + " " +
                    VARIANT1 + COMMA_SEP + " " +
                    VARIANT2 + COMMA_SEP + " " +
                    CORRECT_VARIANT + COMMA_SEP + " " +
                    EXPLANATION + COMMA_SEP + " " +
                    GRADE + COMMA_SEP + " " +
                    IS_VISIBLE


    internal object WordColumn {
        val WORD_ID = "word_id"
        val WORD_MISSING = "missing_word"
        val WORD_FILLED = "filled_word"
        val VARIANT1 = "first_variant"
        val VARIANT2 = "second_variant"
        val CORRECT_VARIANT = "correct_variant"
        val EXPLANATION = "explanation"
        val GRADE = "grade"
        val IS_VISIBLE = "is_visible"
    }

    internal object CategoryColumn {
        val CATEGORY_ID = "category_id"
        val NAME = "name"
    }

    internal object JoinColumn {
        val JOIN_WORD_ID = "join_" + WORD_ID
        val JOIN_CATEGORY_ID = "join_" + CATEGORY_ID
    }
}// To prevent someone from accidentally instantiating the contract class, make the constructor private.

