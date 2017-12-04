package cz.muni.fi.umimecesky.labyrinth

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import cz.muni.fi.umimecesky.R

class LabyrinthActivity : Activity() {

    private lateinit var simulationView: SimulationView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
