package cz.muni.fi.umimecesky.flappygame

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.ballgame.headingTextView
import cz.muni.fi.umimecesky.enums.Difficulty
import cz.muni.fi.umimecesky.enums.Difficulty.Companion.difficulties
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.Constant
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.padding
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class FlappySettingsDialog {

    private val context: Context
    private val raceConcept: RaceConcept

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(context: Context, raceConcept: RaceConcept?) {
        this.context = context
        if (raceConcept == null) throw IllegalArgumentException("Race concept is null")
        this.raceConcept = raceConcept

        context.alert {
            title = context.resources.getString(R.string.settings)
            positiveButton("Ok") {
                context.startActivity<JumpGameActivity>(
                        Constant.FLAPPY_CHOSEN_CATEGORY to raceConcept)
            }
            customView {
                verticalLayout {
                    padding = dip(10)
                    lateinit var fps: TextView
                    linearLayout {
                        headingTextView(context.getString(R.string.fps_per_second)).lparams { leftMargin = dip(5) }
                        fps = headingTextView("${prefs.flappyFps}")
                    }
                    textView("Větší hodnota = rychlejší včela") { padding = dip(8) }

                    addSeekBar(fps)

                    headingTextView("Náročnost slov").lparams { leftMargin = dip(5) }

                    val list = difficulties.toMutableList()
                    list.remove(unavailableDifficulty())


                    createRadioButtons(list.map { it.name }) { index, button ->
                        button?.setOnClickListener { prefs.flappyGradeName = list[index] }
                    }
                }
            }
        }.show()
    }

    private fun @AnkoViewDslMarker _LinearLayout.addSeekBar(fps: TextView) {
        seekBar {
            // 25 - 50 by step 5
            max = 5
            progress = (prefs.flappyFps - 25) / 5

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val currentValue = progress * 5 + 25
                    fps.text = "$currentValue"
                    prefs.flappyFps = currentValue
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    // todo add 3 jump options

    private fun @AnkoViewDslMarker _LinearLayout.createRadioButtons(list: List<String>, function: (Int, RadioButton?) -> Unit) {
        val hardnessRB = arrayOfNulls<RadioButton>(list.size)
        radioGroup {
            orientation = LinearLayout.HORIZONTAL
            for ((i, text) in list.withIndex()) {
                hardnessRB[i] = radioButton()
                textView(text)
            }

            hardnessRB[list.indexOf(getDifficulty().name)]?.isChecked = true
            hardnessRB.forEachIndexed(function)
        }
    }

    private fun getDifficulty(): Difficulty {
        val flappyWordGrade = prefs.flappyGradeName
        Log.i("flappyGradeName", "$flappyWordGrade")
        return if (flappyWordGrade == unavailableDifficulty()) Difficulty.Medium // grade 2 is always available
        else flappyWordGrade
    }

    /**
     * Weird bug - String#equals not working properly ?!
     * Temp fix with constants
     */
    private fun unavailableDifficulty() = when (raceConcept.name) {
        RaceConcept.SPECIAL_E -> Difficulty.Hard
        RaceConcept.DOUBLED -> Difficulty.Hard
        RaceConcept.CASES -> Difficulty.Easy
        RaceConcept.SHORTCUTS -> Difficulty.Easy
        else -> Difficulty.Unknown
    }

}