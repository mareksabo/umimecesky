package cz.muni.fi.umimecesky.labyrinth

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.prefs

class HoleGameActivity : Activity() {

    private lateinit var simulationView: SimulationView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =
                when(prefs.rotationMode) {
                    0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    1 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    2 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    3 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        simulationView = SimulationView(this)
        simulationView.setBackgroundResource(R.drawable.wood)
        setContentView(simulationView)
    }

    override fun onResume() {
        super.onResume()
        simulationView.startSimulation()
    }

    override fun onPause() {
        super.onPause()
        simulationView.stopSimulation()
    }

}
