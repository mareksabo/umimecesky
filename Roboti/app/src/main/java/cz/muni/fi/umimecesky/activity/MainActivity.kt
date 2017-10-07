package cz.muni.fi.umimecesky.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.task.WordImportAsyncTask
import cz.muni.fi.umimecesky.utils.Constant.IS_FILLED
import cz.muni.fi.umimecesky.utils.GuiUtil
import cz.muni.fi.umimecesky.utils.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = Util.getSharedPreferences(this)
        setupButtons()

        if (!sharedPref.getBoolean(IS_FILLED, false)) {
            WordImportAsyncTask(this@MainActivity).execute()
        } // TODO: create loading screen with importing data progress
    }

    private fun setupButtons() {
        setupTrainingButton()
        setupRaceButton()
    }

    private fun setupTrainingButton() {
        GuiUtil.setDefaultColor(trainingButton)
        trainingButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListCategoriesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRaceButton() {
        raceButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LevelRaceActivity::class.java)
            startActivity(intent)
        }

        GuiUtil.setDefaultColor(raceButton)
    }

}
