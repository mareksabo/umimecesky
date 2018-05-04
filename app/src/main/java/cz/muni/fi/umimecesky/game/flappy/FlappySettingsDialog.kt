/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.flappy

import android.content.Context
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.ball.headingTextView
import cz.muni.fi.umimecesky.enums.Difficulty
import cz.muni.fi.umimecesky.enums.Difficulty.Companion.difficultyNames
import cz.muni.fi.umimecesky.enums.Gap.Companion.gaps
import cz.muni.fi.umimecesky.enums.toDifficulty
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.game.shared.util.Constant
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.padding
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class FlappySettingsDialog {

    private val context: Context
    private val raceConcept: RaceConcept

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(context: Context, raceConcept: RaceConcept?) {
        if (raceConcept == null) throw IllegalArgumentException("Race concept is null")
        this.context = context
        this.raceConcept = raceConcept

        if (!prefs.isFlappyGameIntroduced) runGame()
        else showDialog()
    }

    private fun runGame() = context.startActivity<JumpGameActivity>(
            Constant.FLAPPY_CHOSEN_CATEGORY to raceConcept)

    private fun showDialog() {
        context.alert {
            title = context.resources.getString(R.string.settings)
            positiveButton("Ok") { runGame() }
            customView {
                scrollView {
                    verticalLayout {
                        padding = dip(10)

                        difficultyBlock()
                        fpsBlock()
                        gapBlock()
                    }
                }
            }
        }.show()
    }

    private fun @AnkoViewDslMarker _LinearLayout.gapBlock() {
        heading(context.getString(R.string.gap_size))
        createRadioButtons(
                gaps.map { it.name },
                prefs.flappyGap.name,
                { index, button -> button?.setOnClickListener { prefs.flappyGap = gaps[index] } }
        )
    }

    private fun @AnkoViewDslMarker _LinearLayout.fpsBlock() {
        val speedNames = mapOf(30 to "Pomalá", 35 to "Normální", 40 to "Rychlá", 45 to "Turbo")
        lateinit var fps: TextView
        linearLayout {
            heading(context.getString(R.string.flappy_speed))
            fps = heading("${speedNames[prefs.flappyFps]}")
        }
        addSeekBar(fps, min = 30, max = 45, step = 5, speedNames = speedNames)
    }

    private fun @AnkoViewDslMarker _LinearLayout.difficultyBlock() {
        heading(context.getString(R.string.word_difficulty))
        val list = difficultyNames.toMutableList()
        list.remove(unavailableDifficulty().name)
        createRadioButtons(list, storedDifficulty().name, { index, button ->
            button?.setOnClickListener { prefs.flappyGradeName = list[index].toDifficulty() }
        })
    }

    private fun @AnkoViewDslMarker _LinearLayout.addSeekBar(fps: TextView, min: Int, max: Int,
                                                            step: Int, speedNames: Map<Int, String>) {
        seekBar {
            this.max = (max - min) / step
            progress = (prefs.flappyFps - min) / step

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val currentValue = progress * step + min
                    fps.text = speedNames[currentValue]
                    prefs.flappyFps = currentValue
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun @AnkoViewDslMarker _LinearLayout.createRadioButtons(list: List<String>,
                                                                    checkedItem: String,
                                                                    function: (Int, RadioButton?) -> Unit) {
        val hardnessRB = arrayOfNulls<RadioButton>(list.size)
        radioGroup {
            orientation = LinearLayout.HORIZONTAL
            for ((i, text) in list.withIndex()) {
                hardnessRB[i] = radioButton()
                textView(text)
            }

            hardnessRB[list.indexOf(checkedItem)]?.isChecked = true
            hardnessRB.forEachIndexed(function)
        }
    }

    private fun storedDifficulty(): Difficulty {
        if (prefs.flappyGradeName == unavailableDifficulty()) prefs.flappyGradeName = Difficulty.Medium
        return prefs.flappyGradeName
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

    private fun @AnkoViewDslMarker _LinearLayout.heading(text: CharSequence): TextView =
            headingTextView(text, 5)

}