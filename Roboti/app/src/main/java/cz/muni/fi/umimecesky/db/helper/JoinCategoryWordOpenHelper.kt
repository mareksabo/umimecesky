package cz.muni.fi.umimecesky.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import cz.muni.fi.umimecesky.db.DbContract
import cz.muni.fi.umimecesky.db.DbContract.ALL_CATEGORY_COLUMNS
import cz.muni.fi.umimecesky.db.DbContract.ALL_WORD_COLUMNS
import cz.muni.fi.umimecesky.db.DbContract.JOIN_ALL_TABLES
import cz.muni.fi.umimecesky.db.DbContract.JOIN_SAME_ROWS
import cz.muni.fi.umimecesky.db.DbContract.JOIN_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_WORD_ID
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.random
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.NOT_NULL
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select
import java.util.*

val Context.joinCategoryWordOpenHelper: JoinCategoryWordOpenHelper
    get() = JoinCategoryWordOpenHelper.getInstance(applicationContext)

/**
 * @author Marek Sabo
 */
class JoinCategoryWordOpenHelper(context: Context) : ManagedSQLiteOpenHelper(
        context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION) {
    companion object {
        private var instance: JoinCategoryWordOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): JoinCategoryWordOpenHelper {
            if (instance == null) {
                instance = JoinCategoryWordOpenHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(JOIN_TABLE_NAME, true,
                JOIN_WORD_ID to INTEGER + PRIMARY_KEY + NOT_NULL,
                JOIN_CATEGORY_ID to INTEGER + NOT_NULL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(JOIN_TABLE_NAME, true)
        onCreate(db)
    }

    fun getWords(category: Category): List<FillWord> = use {
        select(JOIN_ALL_TABLES, ALL_WORD_COLUMNS)
                .whereArgs(
                        "$JOIN_SAME_ROWS AND $JOIN_TABLE_NAME.$JOIN_CATEGORY_ID = {categoryId}",
                        "categoryId" to category.id)
                .exec { parseList(classParser()) }
    }

    fun getCategory(fillWord: FillWord): Category = use {
        select(JOIN_ALL_TABLES, ALL_CATEGORY_COLUMNS)
                .whereArgs("$JOIN_SAME_ROWS AND $JOIN_TABLE_NAME.$JOIN_WORD_ID = {wordId}",
                        "wordId" to fillWord.id)
                .exec { parseSingle(classParser()) }
    }

    fun getRandomCategoryWord(raceConcept: RaceConcept): FillWord =
            randomCategoryWord(raceConcept.categoryIDs)

    fun getRandomCategoryWord(categories: List<Category>): FillWord =
            randomCategoryWord(categories.map { it.id })

    private fun randomCategoryWord(categories: List<Int>): FillWord {
        if (categories != storedCategories) {
            storedWords = getWordsInCategories(categories) as MutableList<FillWord>
            storedCategories = categories
        }
        return getRandomWord(categories)
    }

    private var storedWords: MutableList<FillWord> = ArrayList()
    private var storedCategories: List<Int> = ArrayList() // TODO: use UniqueRandom

    private fun getWordsInCategories(categoryIds: List<Int>): List<FillWord> = use {
        select(JOIN_ALL_TABLES, ALL_WORD_COLUMNS)
                .whereSimple("$JOIN_SAME_ROWS AND ($JOIN_TABLE_NAME.$JOIN_CATEGORY_ID IN (${categoryIds.joinToString()}) )")
                .exec { parseList(classParser()) }
    }

    private fun getRandomWord(categories: List<Int>): FillWord {
        if (storedWords.isEmpty()) storedWords = getWordsInCategories(categories) as MutableList<FillWord>
        Log.d("storedWords", "$storedWords")

        val index = random.nextInt(storedWords.size)
        return storedWords.removeAt(index)
    }
}
