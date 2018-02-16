package cz.muni.fi.umimecesky.labyrinth

import android.app.Activity
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewManager
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.padding
import org.jetbrains.anko.px2dip
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import kotlin.math.roundToInt

class HoleGameActivity : Activity() {

    companion object {
        private val difficultyTypes = arrayOf("Lehké", "Mírné", "Težší", "Náročné")
    }

    private lateinit var simulationView: SimulationView
    private var hardnessRB: Array<RadioButton> = emptyArray()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var isHoleCountChanged: Boolean = false
    private var dialog: DialogInterface? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =
                when (prefs.rotationMode) {
                    0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    1 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    2 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    3 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        simulationView = SimulationView(this)
        simulationView.setBackgroundResource(R.drawable.wood)
        setContentView(simulationView)


        dialog = alert {
            title = resources.getString(R.string.action_settings)
            positiveButton("Ok") { }
            customView {
                verticalLayout {
                    padding = dip(10)
                    lateinit var holesCount: TextView
                    linearLayout {
                        headingTextView("Počet děr:").lparams { leftMargin = dip(5) }
                        holesCount = headingTextView("${prefs.holesAmount}")
                    }

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
                                isHoleCountChanged = true
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    }

                    headingTextView("Náročnost slov").lparams { leftMargin = dip(5) }

                    radioGroup {
                        orientation = LinearLayout.HORIZONTAL
                        val easy = radioButton()
                        textView(difficultyTypes[0])
                        val medium = radioButton()
                        textView(difficultyTypes[1])
                        val hard = radioButton()
                        textView(difficultyTypes[2])
                        val extreme = radioButton()
                        textView(difficultyTypes[3])

                        hardnessRB = arrayOf(easy, medium, hard, extreme)
                        hardnessRB[prefs.holeWordGrade].isChecked = true
                    }
                }
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        simulationView.startSimulation()
    }

    override fun onPause() {
        super.onPause()
        simulationView.stopSimulation()
        dialog?.cancel()
        logChangedButtons()
    }

    private fun logChangedButtons() {
        var alreadyLogged = false
        if (hardnessRB.isNotEmpty()) {
            val oldValue = prefs.holeWordGrade
            val newValue = hardnessRB.map { it.isChecked }.indexOf(true)

            prefs.holeWordGrade = newValue
            if (oldValue != newValue) {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, difficultyTypes[newValue])
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                logBoth()
                alreadyLogged = true
            }
        }
        if (isHoleCountChanged) {
            val bundle = Bundle()
            bundle.putInt(FirebaseAnalytics.Param.QUANTITY, prefs.holesAmount)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            if (!alreadyLogged) logBoth()
        }
    }

    private fun logBoth() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                difficultyTypes[prefs.holeWordGrade] + prefs.holesAmount)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

}

fun ViewManager.headingTextView(text: CharSequence): TextView =
        textView(text) {
            padding = dip(10)
            typeface = Typeface.DEFAULT_BOLD
            textSize = px2dip(textSize.roundToInt()) + 3f
        }