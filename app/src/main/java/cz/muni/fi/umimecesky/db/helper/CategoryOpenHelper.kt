/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cz.muni.fi.umimecesky.db.DbContract
import cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_NAME
import cz.muni.fi.umimecesky.game.practise.Category
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.NOT_NULL
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.UNIQUE
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.parseList
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

    fun allCategories(): List<Category> = use {
        select(CATEGORY_TABLE_NAME).exec {
            parseList(classParser())
        }
    }
}
