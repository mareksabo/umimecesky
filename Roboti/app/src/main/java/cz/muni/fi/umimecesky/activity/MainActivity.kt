package cz.muni.fi.umimecesky.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import cz.muni.fi.umimecesky.labyrinth.LabyrinthActivity
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import kotlinx.android.synthetic.main.activity_main.holeButton
import kotlinx.android.synthetic.main.activity_main.raceButton
import kotlinx.android.synthetic.main.activity_main.trainingButton
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtons()

        if (!prefs.isImported) importDataAsynchronously()
    }

    private fun importDataAsynchronously() {
        doAsync {
            @Suppress("DEPRECATION")
            lateinit var dialog: ProgressDialog
            runOnUiThread {
                dialog = indeterminateProgressDialog(getString(R.string.loading_data))
                dialog.setCancelable(false)
            }
            createTables()
            importCategories()
            importWords()
            importConversions()
            prefs.isImported = true

            runOnUiThread( { dialog.cancel() } )

            Category(1, "aa")
            FillWord(1, "", "", "", "", 0, "", 1)
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

    @Throws(IOException::class)
    private fun openFileIgnoreFirstLine(fileName: String): BufferedReader {

        val inStream = assets.open(fileName)
        val buffer = BufferedReader(InputStreamReader(inStream))
        buffer.readLine() // column names

        return buffer
    }

    private fun setupButtons() {
        trainingButton.setOnClickListener { startActivity<ListCategoriesActivity>() }
        raceButton.setOnClickListener { startActivity<LevelRaceActivity>() }
        holeButton.setOnClickListener { startActivity<LabyrinthActivity>() }
    }

}
