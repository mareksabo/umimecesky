package cz.muni.fi.umimecesky.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cz.muni.fi.umimecesky.db.DbContract
import cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_NAME
import cz.muni.fi.umimecesky.pojo.Category
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.NOT_NULL
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.UNIQUE
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

val Context.categoryOpenHelper: CategoryOpenHelper
    get() = CategoryOpenHelper.getInstance(applicationContext)

/**
 * @author Marek Sabo
 */
class CategoryOpenHelper(context: Context) : ManagedSQLiteOpenHelper(
        context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION) {
    companion object {
        private var instance: CategoryOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): CategoryOpenHelper {
            if (instance == null) {
                instance = CategoryOpenHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(CATEGORY_TABLE_NAME, true,
                CATEGORY_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                CATEGORY_NAME to TEXT + NOT_NULL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(CATEGORY_TABLE_NAME, true)
        onCreate(db)
    }

    fun findCategory(id: Int): Category = use {
        select(CATEGORY_TABLE_NAME).whereSimple("$CATEGORY_ID = ?", id.toString())
                .exec {
                    val parser = rowParser { id: Int, name: String -> Pair(id, name) }
                    parseSingle(classParser())
                }
    }

    fun findCategory(): Int = use {
        select(CATEGORY_TABLE_NAME, CATEGORY_ID).whereSimple("$CATEGORY_ID = 1")
                .exec { parseSingle(IntParser) }
    }

    fun allCategories(): List<Category> = use {
        select(CATEGORY_TABLE_NAME).exec {
            parseList(classParser())
        }
    }
}

class MyRowParser : RowParser<Pair<Int, String>> {
    override fun parseRow(columns: Array<Any?>): Pair<Int, String> {
        return Pair(columns[0] as Int, columns[1] as String)
    }
}

