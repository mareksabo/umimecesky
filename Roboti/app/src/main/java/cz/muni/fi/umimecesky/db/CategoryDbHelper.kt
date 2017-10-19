package cz.muni.fi.umimecesky.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE
import cz.muni.fi.umimecesky.db.DbContract.COMMA_SEP
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.NAME
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_NAME
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION
import cz.muni.fi.umimecesky.pojo.Category
import java.util.*


class CategoryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CATEGORY_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(CATEGORY_DELETE_TABLE)
        onCreate(db)
    }


    fun addCategory(db: SQLiteDatabase, id: Long, name: String): Boolean {
        val contentValues = ContentValues()
        contentValues.put(CATEGORY_ID, id)
        contentValues.put(NAME, name)
        val wordId = db.insert(CATEGORY_TABLE, null, contentValues)
        return wordId != -1L
    }

    fun findCategory(id: Long): Category? {
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $CATEGORY_TABLE WHERE $CATEGORY_ID = $id"
        val cursor = db.rawQuery(selectQuery, null)
        val category: Category?
        if (cursor.moveToFirst()) {
            category = cursorToCategory(cursor)
        } else {
            category = null
            Log.d("Category not found,id", id.toString())
        }

        cursor.close()
        db.close()
        return category
    }

    val allCategories: List<Category>
        get() {
            val categoryList = ArrayList<Category>()

            val selectQuery = "SELECT  * FROM " + CATEGORY_TABLE

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    categoryList.add(cursorToCategory(cursor))
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return categoryList
        }

    private fun cursorToCategory(cursor: Cursor): Category =
            Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1))

    companion object {


        private val CATEGORY_CREATE_TABLE =
                "CREATE TABLE " + CATEGORY_TABLE + " ( " +
                        CATEGORY_ID + " INT PRIMARY KEY " + COMMA_SEP +
                        NAME + " TEXT NOT NULL " + " )"

        private val CATEGORY_DELETE_TABLE = "DROP TABLE IF EXISTS " + CATEGORY_TABLE
    }
}
