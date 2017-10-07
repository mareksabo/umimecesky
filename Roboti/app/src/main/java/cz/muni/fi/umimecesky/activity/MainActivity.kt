package cz.muni.fi.umimecesky.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.task.WordImportAsyncTask
import cz.muni.fi.umimecesky.utils.GuiUtil
import cz.muni.fi.umimecesky.utils.Util

import cz.muni.fi.umimecesky.utils.Constant.IS_FILLED

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
        val trainingButton = findViewById(R.id.trainingButton) as Button
        GuiUtil.setDefaultColor(trainingButton)
        trainingButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListCategoriesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRaceButton() {
        val raceButton = findViewById(R.id.raceButton) as Button
        raceButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LevelRaceActivity::class.java)
            startActivity(intent)
        }

        GuiUtil.setDefaultColor(raceButton)
    }

}
