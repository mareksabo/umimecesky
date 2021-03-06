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

package cz.muni.fi.umimecesky.game.mainmenu

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.LinearLayout
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_NAME
import cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION
import cz.muni.fi.umimecesky.db.DbContract.JOIN_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_CATEGORY_ID
import cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_WORD_ID
import cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE_NAME
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.FILLED_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.MISSING_WORD
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2
import cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID
import cz.muni.fi.umimecesky.db.helper.categoryOpenHelper
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.db.helper.wordOpenHelper
import cz.muni.fi.umimecesky.game.ball.HoleGameActivity
import cz.muni.fi.umimecesky.game.flappy.FlappyListCategoriesActivity
import cz.muni.fi.umimecesky.game.practise.Category
import cz.muni.fi.umimecesky.game.practise.ListCategoriesActivity
import cz.muni.fi.umimecesky.game.robots.LevelRaceActivity
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.util.Constant.UNSET_ID
import cz.muni.fi.umimecesky.prefs
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import me.toptas.fancyshowcase.FancyShowCaseView
import org.jetbrains.anko.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

// visible words: 4505
// grade 0: 98
// grade 1: 1534
// grade 2: 1916
// grade 3: 957

    private var dialog: ProgressDialog? = null
    private var doAsyncTask: Future<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtons()
        setAllButtonsSameWidth()
        Log.i("prefs", "${prefs.flappyGradeName.grade}")

        if (configuration.landscape) robotIcon.visibility = View.GONE

        if (prefs.userId == UNSET_ID) prefs.userId = System.currentTimeMillis()

        if (!isDbImported()) {
            importDataAsynchronously()
        } else {
            removeEmptyCategory()
            prefs.holeWordGrade = 1 // todo: to be removed
        }
    }

    private fun setupButtons() {
        trainingButton.setOnClickListener { startActivity<ListCategoriesActivity>() }
        raceButton.setOnClickListener { startActivity<LevelRaceActivity>() }
        holeButton.setOnClickListener { startActivity<HoleGameActivity>() }
        jumpButton.setOnClickListener { startActivity<FlappyListCategoriesActivity>() }
    }

    private fun setAllButtonsSameWidth() {
        val layout = activity_main as LinearLayout
        layout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val maxButtonWidth = (0 until layout.childCount)
                        .map { layout.getChildAt(it) }
                        .filterIsInstance<Button>()
                        .map { it.width }
                        .max()!!

                (0 until layout.childCount)
                        .map { layout.getChildAt(it) }
                        .filterIsInstance<Button>()
                        .forEach { it.width = maxButtonWidth }
            }
        })
    }

    private fun isDbImported(): Boolean = try {
        val categories = categoryOpenHelper.allCategories()
        val word = wordOpenHelper.getRandomWord(1)
        categories.isNotEmpty() && word.wordMissing.isNotEmpty()
    } catch (e: SQLiteException) {
        false
    }

    private fun importDataAsynchronously() {
        doAsyncTask = doAsync {
            runOnUiThread {
                dialog = indeterminateProgressDialog(getString(R.string.loading_data))
                dialog?.setCancelable(false)
            }
            createTables()
            importCategories()
            importWords()
            importConversions()

            runOnUiThread({
                dialog?.isShowing.let { dialog?.dismiss() }
                showHintOnce()
            })

            Category(1, "aa")
            FillWord(1, "", "", "", "", 0, "", 1)
        }

    }

    private fun showHintOnce() {
        Observable.just("a")
                .delay(50L, TimeUnit.MILLISECONDS)
                .subscribe {
                    FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.action_settings_item))
                            .title(getString(R.string.hint_settings_text))
                            .showOnce("settingsHint")
                            .build()
                            .show()
                }
    }

    private fun createTables() {
        val db = categoryOpenHelper.writableDatabase
        categoryOpenHelper.onUpgrade(db, DATABASE_VERSION - 1, DATABASE_VERSION)
        wordOpenHelper.onUpgrade(db, DATABASE_VERSION - 1, DATABASE_VERSION)
        joinCategoryWordOpenHelper.onUpgrade(db, DATABASE_VERSION - 1, DATABASE_VERSION)
    }

    @Throws(IOException::class)
    private fun importCategories() {

        categoryOpenHelper.use {
            transaction {
                val buffer = openFileIgnoreFirstLine("concepts.csv")
                while (buffer.ready()) {
                    val columns = buffer.readLine()
                            .split(";")
                            .dropLastWhile({ it.isEmpty() })

                    insert(CATEGORY_TABLE_NAME,
                            CATEGORY_ID to columns[0],
                            CATEGORY_NAME to columns[1]
                    )
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun importWords() {

        wordOpenHelper.use {
            transaction {
                val buffer = openFileIgnoreFirstLine("doplnovacka_word.csv")
                while (buffer.ready()) {
                    val columns = buffer.readLine()
                            .split(";")
                            .dropLastWhile({ it.isEmpty() })

                    // ignoring "not visible" words
                    if (columns[8] == "0") continue

                    insert(WORD_TABLE_NAME,
                            WORD_ID to columns[0],
                            MISSING_WORD to columns[1],
                            FILLED_WORD to columns[2],
                            VARIANT1 to columns[3],
                            VARIANT2 to columns[4],
                            CORRECT_VARIANT to columns[5],
                            EXPLANATION to columns[6],
                            GRADE to columns[7]
                    )
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun importConversions() {

        joinCategoryWordOpenHelper.use {
            transaction {
                val buffer = openFileIgnoreFirstLine("doplnovacka_concept_word.csv")

                while (buffer.ready()) {
                    val columns = buffer.readLine()
                            .split(";")
                            .dropLastWhile({ it.isEmpty() })

                    insert(JOIN_TABLE_NAME,
                            JOIN_CATEGORY_ID to columns[0],
                            JOIN_WORD_ID to columns[1]
                    )
                }
            }
        }
    }

    private fun removeEmptyCategory() {
        categoryOpenHelper.use {
            delete(CATEGORY_TABLE_NAME, "$CATEGORY_ID = 37")
        }
    }

    @Throws(IOException::class)
    private fun openFileIgnoreFirstLine(fileName: String): BufferedReader {

        val inStream = assets.open(fileName)
        val buffer = BufferedReader(InputStreamReader(inStream))
        buffer.readLine() // column names

        return buffer
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings_item -> {
                startActivity<SettingsActivity>()
                true
            }
            R.id.rate_app_item -> {
                openPlayStore()
                true
            }
            R.id.send_email_item -> {
                email("marek.sabo.gvpt@gmail.com", "Umíme česky - problém")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun openPlayStore() {
        val goToMarket = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        // To count with Play market back stack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            browse("http://play.google.com/store/apps/details?id=$packageName")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.let { it.isShowing.let { dialog?.dismiss() } }
        doAsyncTask?.cancel(true)
    }

}
