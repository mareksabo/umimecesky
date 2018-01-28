package cz.muni.fi.umimecesky.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cz.muni.fi.umimecesky.db.DbContract
import cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.FILLED_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.MISSING_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.pojo.FillWord
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.NOT_NULL
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.UNIQUE
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select


val Context.wordOpenHelper: WordOpenHelper
    get() = WordOpenHelper.getInstance(applicationContext)
/**
 * @author Marek Sabo
 */
class WordOpenHelper(context: Context) : ManagedSQLiteOpenHelper(
        context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION) {
    companion object {
        private var instance: WordOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): WordOpenHelper {
            if (instance == null) {
                instance = WordOpenHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(WORD_TABLE_NAME, true,
                WORD_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                MISSING_WORD to TEXT + NOT_NULL,
                FILLED_WORD to TEXT + NOT_NULL,
                VARIANT1 to TEXT + NOT_NULL,
                VARIANT2 to TEXT + NOT_NULL,
                CORRECT_VARIANT to INTEGER + NOT_NULL,
                EXPLANATION to TEXT + NOT_NULL,
                GRADE to INTEGER + NOT_NULL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(WORD_TABLE_NAME, true)
        onCreate(db)
    }

    fun getWord(id: Int) : FillWord = use {
        select(WORD_TABLE_NAME)
                .whereSimple("$WORD_ID = ?", id.toString())
                .exec { parseSingle(classParser()) }
    }

    fun getRandomWord() : FillWord = use {
        select(WORD_TABLE_NAME)
                .orderBy("RANDOM()")
                .limit(1)
                .exec { parseSingle(classParser()) }
    }
}