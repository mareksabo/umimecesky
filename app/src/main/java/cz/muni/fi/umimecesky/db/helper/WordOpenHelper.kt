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
import cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.FILLED_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.MISSING_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.game.shared.model.FillWord
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

    fun getRandomWord(grade: Int) : FillWord = use {
        select(WORD_TABLE_NAME)
                .whereSimple("$GRADE = ?", "$grade")
                .orderBy("RANDOM()")
                .limit(1)
                .exec { parseSingle(classParser()) }
    }


    // TODO: better fix long answers
    fun getRandomWordWithSmallVariants(grade: Int): FillWord {
        var currentWord: FillWord
        do {
            currentWord = getRandomWord(grade)
        } while (currentWord.variant1.length >= 3 || currentWord.variant2.length >= 3)
        return currentWord
    }
}