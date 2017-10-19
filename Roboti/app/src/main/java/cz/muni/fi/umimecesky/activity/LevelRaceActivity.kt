package cz.muni.fi.umimecesky.activity


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.adapterlistener.LevelAdapter
import cz.muni.fi.umimecesky.utils.Constant
import cz.muni.fi.umimecesky.utils.GuiUtil
import cz.muni.fi.umimecesky.utils.WebUtil.getWebConcepts
import kotlinx.android.synthetic.main.activity_race_levels.levelListView

class LevelRaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race_levels)
    }

    override fun onStart() {
        // TODO: replace with data update - notify?
        super.onStart()

        levelListView.layoutManager = LinearLayoutManager(applicationContext)
        levelListView.itemAnimator = DefaultItemAnimator()

        levelListView.adapter =
                LevelAdapter(getWebConcepts(this), { raceConcept ->
                    val intent = Intent(this@LevelRaceActivity, RaceActivity::class.java)
                    intent.putExtra(Constant.RACE_CONCEPT_EXTRA, raceConcept)
                    startActivity(intent)
                })

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) GuiUtil.hideNavigationBar(this)
    }

}
