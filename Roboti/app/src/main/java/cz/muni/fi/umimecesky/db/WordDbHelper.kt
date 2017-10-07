package cz.muni.fi.umimecesky.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cz.muni.fi.umimecesky.db.DbContract.COMMA_SEP
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_NAME
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION
import cz.muni.fi.umimecesky.db.DbContract.VISIBLE_TRUE
import cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.IS_VISIBLE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_FILLED
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_MISSING
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.utils.Conversion.convertCursorToFillWord

class WordDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addFilledWord(db: SQLiteDatabase, id: Long, missingWord: String, filledWord: String,
                      variant1: String, variant2: String, correctVariant: String,
                      explanation: String, grade: Int, visibility: Boolean): Boolean {
        val contentValues = ContentValues()
        contentValues.put(WORD_ID, id)
        contentValues.put(WORD_MISSING, missingWord)
        contentValues.put(WORD_FILLED, filledWord)
        contentValues.put(VARIANT1, variant1)
        contentValues.put(VARIANT2, variant2)
        contentValues.put(CORRECT_VARIANT, correctVariant)
        contentValues.put(EXPLANATION, explanation)
        contentValues.put(GRADE, grade)
        contentValues.put(IS_VISIBLE, visibility)
        val wordId = db.insert(WORD_TABLE, null, contentValues)
        return wordId != -1L
    }

    fun findWord(filledName: String): FillWord? {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM " + WORD_TABLE + " WHERE "
                + WORD_FILLED + " = ?", arrayOf(filledName))
        if (!c.moveToFirst()) {
            return null
        }
        return convertCursorToFillWord(c)
    }

    val randomFilledWord: FillWord
        get() {
            val db = this.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM " + WORD_TABLE
                    + " WHERE " + IS_VISIBLE + " = " + VISIBLE_TRUE
                    + " ORDER BY RANDOM() LIMIT 1", null)
            !cursor.moveToFirst()
//                return null

            return convertCursorToFillWord(cursor)

        }

    companion object {

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + WORD_TABLE + " ( " +
                        WORD_ID + " LONG PRIMARY KEY " + COMMA_SEP +
                        WORD_MISSING + " TEXT NOT NULL " + COMMA_SEP +
                        WORD_FILLED + " TEXT NOT NULL " + COMMA_SEP +
                        VARIANT1 + " TEXT NOT NULL " + COMMA_SEP +
                        VARIANT2 + " TEXT NOT NULL " + COMMA_SEP +
                        CORRECT_VARIANT + " INTEGER NOT NULL " + COMMA_SEP +
                        EXPLANATION + " TEXT NOT NULL " + COMMA_SEP +
                        GRADE + " INTEGER NOT NULL " + COMMA_SEP +
                        IS_VISIBLE + " INTEGER NOT NULL " +
                        " )"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + WORD_TABLE
    }


}
