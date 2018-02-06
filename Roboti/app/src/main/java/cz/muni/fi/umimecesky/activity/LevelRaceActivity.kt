package cz.muni.fi.umimecesky.activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.adapterlistener.LevelAdapter
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.GuiUtil.hideNavigationBar
import kotlinx.android.synthetic.main.activity_race_levels.levelListView
import org.jetbrains.anko.startActivity

class LevelRaceActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race_levels)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStart() {
        // TODO: replace with data update - notify?
        super.onStart()

        levelListView.layoutManager = LinearLayoutManager(applicationContext)
        levelListView.itemAnimator = DefaultItemAnimator()

        levelListView.adapter =
                LevelAdapter(prefs.maxRobotsCategories, { raceConcept ->

                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, raceConcept.name)
                    bundle.putString(FirebaseAnalytics.Param.VALUE, raceConcept.currentLevel.toString())
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, raceConcept.toString())
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                    prefs.currentRobotConcept = raceConcept
                    startActivity<RaceActivity>()
                })

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideNavigationBar()
    }

}
