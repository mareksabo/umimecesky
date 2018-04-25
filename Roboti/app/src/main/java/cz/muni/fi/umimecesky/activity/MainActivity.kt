@file:Suppress("DEPRECATION")

package cz.muni.fi.umimecesky.activity

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import cz.muni.fi.umimecesky.ballgame.Dimensions.isTablet
import cz.muni.fi.umimecesky.ballgame.HoleGameActivity
import cz.muni.fi.umimecesky.ballgame.HoleGameLogger
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.activity_main
import kotlinx.android.synthetic.main.activity_main.holeButton
import kotlinx.android.synthetic.main.activity_main.raceButton
import kotlinx.android.synthetic.main.activity_main.robotIcon
import kotlinx.android.synthetic.main.activity_main.trainingButton
import me.toptas.fancyshowcase.FancyShowCaseView
import org.jetbrains.anko.browse
import org.jetbrains.anko.configuration
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.email
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.landscape
import org.jetbrains.anko.startActivity
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

        if (configuration.landscape && !isTablet()) robotIcon.visibility = View.GONE

        if (!isDbImported()) {
            prefs.userId = System.currentTimeMillis()
            importDataAsynchronously()
        } else {
            removeEmptyCategory()
            prefs.holeWordGrade = 1 // todo: to be removed
        }
        HoleGameLogger(this).userPropertiesSetup()
    }

    private fun setupButtons() {
        trainingButton.setOnClickListener { startActivity<ListCategoriesActivity>() }
        raceButton.setOnClickListener { startActivity<LevelRaceActivity>() }
        holeButton.setOnClickListener { startActivity<HoleGameActivity>() }
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
        !categories.isEmpty()
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
