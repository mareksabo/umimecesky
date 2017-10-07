package cz.muni.fi.umimecesky.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.ArrayList
import java.util.Random

import cz.muni.fi.umimecesky.pojo.FillWord

import cz.muni.fi.umimecesky.db.DbContract.ALL_WORD_COLUMNS
import cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE
import cz.muni.fi.umimecesky.db.DbContract.COMMA_SEP
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_NAME
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION
import cz.muni.fi.umimecesky.db.DbContract.JOIN_TABLE
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_WORD_ID
import cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.utils.Conversion.convertCursorToFillWord


class WordCategoryDbHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


    fun addConversion(db: SQLiteDatabase, categoryId: Int, wordId: Long): Boolean {
        val contentValues = ContentValues()
        contentValues.put(JOIN_CATEGORY_ID, categoryId)
        contentValues.put(JOIN_WORD_ID, wordId)
        val result = db.insert(JOIN_TABLE, null, contentValues)
        return result != -1L
    }

    fun getCategoryId(wordId: Long): Int? {

        val selectQuery = "SELECT $JOIN_CATEGORY_ID FROM $JOIN_TABLE WHERE " +
                JOIN_WORD_ID + " = " + wordId

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        val categoryId: Int
        if (cursor.moveToFirst()) {
            categoryId = Integer.parseInt(cursor.getString(0))
        } else {
            Log.d("Category missing", wordId.toString())
            categoryId = -1
        }

        cursor.close()
        db.close()
        return categoryId
    }

    private var storedWords: MutableList<FillWord> = ArrayList()
    private var storedCategories: List<Int> = ArrayList() // TODO: refactor variables?
    private val random = Random()


    fun getRandomCategoryWord(categoryIDs: List<Int>): FillWord {

        if (!storedWords.isEmpty() && !storedCategories.isEmpty() && categoryIDs == storedCategories) {
            return getRandomWord(storedWords)
        }

        val categoryIds = getCategoryIds(categoryIDs)

        val QUERY = "SELECT " +
                ALL_WORD_COLUMNS +
                " FROM " + JOIN_TABLE + " INNER JOIN " + WORD_TABLE + " INNER JOIN " + CATEGORY_TABLE +
                " WHERE " + JOIN_TABLE + "." + JOIN_WORD_ID + " = " + WORD_TABLE + "." + WORD_ID +
                " AND " + JOIN_TABLE + "." + JOIN_CATEGORY_ID + " = " + CATEGORY_TABLE + "." + CATEGORY_ID +
                " AND " + JOIN_TABLE + "." + JOIN_CATEGORY_ID + " IN " + categoryIds

        val db = this.readableDatabase
        val cursor = db.rawQuery(QUERY, null)

        storedCategories = categoryIDs
        storedWords = ArrayList<FillWord>()

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val word = convertCursorToFillWord(cursor)
            storedWords.add(word)
            cursor.moveToNext()
        }

        cursor.close()
        db.close()
        return getRandomWord(storedWords)
    }

    private fun getRandomWord(storedWords: MutableList<FillWord>): FillWord {
        if (storedWords.isEmpty()) {
            return WordDbHelper(context).randomFilledWord
        }
        val index = random.nextInt(storedWords.size)
        return storedWords.removeAt(index)
    }


    private fun getCategoryIds(categoryIdList: List<Int>): String {

        val builder = StringBuilder("(")
        var delimiter = ""
        for (integer in categoryIdList) {
            builder.append(delimiter).append(integer)
            delimiter = ","
        }
        builder.append(")")
        return builder.toString()
    }

    companion object {

        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + JOIN_TABLE + " ( " +
                JOIN_WORD_ID + " LONG PRIMARY KEY NOT NULL " + COMMA_SEP +
                JOIN_CATEGORY_ID + " INT NOT NULL " + " )"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + JOIN_TABLE
    }

}
