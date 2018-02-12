package cz.muni.fi.umimecesky.activity

import android.app.ProgressDialog
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
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
import cz.muni.fi.umimecesky.labyrinth.HoleGameActivity
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import kotlinx.android.synthetic.main.activity_main.holeButton
import kotlinx.android.synthetic.main.activity_main.raceButton
import kotlinx.android.synthetic.main.activity_main.trainingButton
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.dip
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.padding
import org.jetbrains.anko.px2dip
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.space
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {


    private var hardnessRB: Array<RadioButton> = emptyArray()
    private var randomRB: RadioButton? = null

// visible words: 4505
// grade 0: 98
// grade 1: 1534
// grade 2: 1916
// grade 3: 957


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

            runOnUiThread({ dialog.cancel() })

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
        holeButton.setOnClickListener {

            alert {
                title = resources.getString(R.string.action_settings)
                positiveButton("Ok") { startActivity<HoleGameActivity>() }
                customView {
                    verticalLayout {
                        padding = dip(10)
                        lateinit var holesCount: TextView
                        linearLayout {
                            headingTextView("Počet děr:").lparams { leftMargin = dip(5) }
                            holesCount = headingTextView("${prefs.holesAmount}")
                        }
                        space {}
                        seekBar {
                            val realMin = 5
                            val realMax = 15

                            max = realMax - realMin
                            progress = prefs.holesAmount - realMin
                            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                    val currentValue = progress + realMin
                                    holesCount.text = "$currentValue"
                                    prefs.holesAmount = currentValue
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                            })
                        }

                        headingTextView("Umístnění děr").lparams { leftMargin = dip(5) }

                        radioGroup {
                            orientation = LinearLayout.HORIZONTAL

                            randomRB = radioButton()
                            textView("Náhodné")
                            val hardRB = radioButton()
                            textView("S minimální vzdáleností")

                            if (prefs.holesRandomlyGenerated) randomRB?.isChecked = true
                            else hardRB.isChecked = true
                        }

                        headingTextView("Náročnost slov").lparams { leftMargin = dip(5) }

                        radioGroup {
                            orientation = LinearLayout.HORIZONTAL
                            val easy = radioButton()
                            textView("Lehoučké")
                            val medium = radioButton()
                            textView("Mírné")
                            val hard = radioButton()
                            textView("Težší")
                            val extreme = radioButton()
                            textView("Náročné")

                            hardnessRB = arrayOf(easy, medium, hard, extreme)
                            hardnessRB[prefs.holeWordGrade].isChecked = true
                        }
                    }
                }
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onPause() {
        super.onPause()
        if (hardnessRB.isNotEmpty())
            prefs.holeWordGrade = hardnessRB.map { it.isChecked }.indexOf(true)

        randomRB?.let { prefs.holesRandomlyGenerated = it.isChecked }
    }

    private fun ViewManager.headingTextView(text: CharSequence): TextView =
            textView(text) {
                padding = dip(10)
                typeface = Typeface.DEFAULT_BOLD
                textSize = px2dip(textSize.roundToInt()) + 3f
            }

}
