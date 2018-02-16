package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko.setContentView

/**
 * @author Marek Sabo
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsUI: SettingsUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsUI = SettingsUI()
        settingsUI.setContentView(this)
    }

    override fun onPause() {
        super.onPause()
        prefs.rotationMode = settingsUI.radioButtons.map { it.isChecked }.indexOf(true)
    }
}