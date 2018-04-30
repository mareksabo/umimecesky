package cz.muni.fi.umimecesky.db

import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_NAME
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_WORD_ID
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.FILLED_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.MISSING_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID

object DbContract {

    const val DATABASE_NAME = "FillWords.db"
    // If you change the database schema, you must increment the database version.
    const val DATABASE_VERSION = 3

    //table names
    const val CATEGORY_TABLE_NAME = "Category"
    const val WORD_TABLE_NAME = "Word"
    const val JOIN_TABLE_NAME = "WordCategory"


    const val ALL_WORD_COLUMNS =
                    "$WORD_TABLE_NAME.$WORD_ID, " +
                    "$WORD_TABLE_NAME.$MISSING_WORD, " +
                    "$WORD_TABLE_NAME.$FILLED_WORD, " +
                    "$WORD_TABLE_NAME.$VARIANT1, " +
                    "$WORD_TABLE_NAME.$VARIANT2, " +
                    "$WORD_TABLE_NAME.$CORRECT_VARIANT, " +
                    "$WORD_TABLE_NAME.$EXPLANATION, " +
                    "$WORD_TABLE_NAME.$GRADE"

    const val ALL_CATEGORY_COLUMNS =
            "$CATEGORY_TABLE_NAME.$CATEGORY_ID, $CATEGORY_TABLE_NAME.$CATEGORY_NAME"

    const val JOIN_ALL_TABLES = "$JOIN_TABLE_NAME INNER JOIN $WORD_TABLE_NAME INNER JOIN $CATEGORY_TABLE_NAME"

    const val JOIN_SAME_ROWS =
            "$JOIN_TABLE_NAME.$JOIN_WORD_ID = $WORD_TABLE_NAME.$WORD_ID AND " +
            "$JOIN_TABLE_NAME.$JOIN_CATEGORY_ID = $CATEGORY_TABLE_NAME.$CATEGORY_ID"

    object WordColumn {
        const val WORD_ID = "word_id"
        const val MISSING_WORD = "missing_word"
        const val FILLED_WORD = "filled_word"
        const val VARIANT1 = "first_variant"
        const val VARIANT2 = "second_variant"
        const val CORRECT_VARIANT = "correct_variant"
        const val EXPLANATION = "explanation"
        const val GRADE = "grade"
    }

    object CategoryColumn {
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "name"
    }

    object JoinColumn {
        const val JOIN_WORD_ID = WORD_ID
        const val JOIN_CATEGORY_ID = CATEGORY_ID
    }
}

