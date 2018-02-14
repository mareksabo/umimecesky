package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout.HORIZONTAL
import android.widget.RadioButton
import android.widget.SeekBar
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.labyrinth.headingTextView
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.ctx
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * @author Marek Sabo
 */
class SettingsActivity : AppCompatActivity() {

    private val radioButtons = ArrayList<RadioButton>()

    companion object {
        private val ballWeightOptions = arrayOf("Pírko", "Lehká", "Normální", "Těžká", "Kámen")
        private val weightTypesCount = ballWeightOptions.size - 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scrollView {

            verticalLayout {
                padding = dip(10)

                headingTextView(ctx.getString(R.string.display_rotation)).padding = dip(15)

                linearLayout {
                    adjustedDrawable(R.drawable.phone1)
                    adjustedDrawable(R.drawable.phone3)
                    adjustedDrawable(R.drawable.phone2)
                    adjustedDrawable(R.drawable.phone4)
                }

                radioGroup {
                    orientation = HORIZONTAL

                    repeat(4) {
                        // TODO: fix radio button center
                        radioButtons.add(radioButton {
                            gravity = Gravity.CENTER
                        }.lparams {
                            width = matchParent
                            weight = 1f
                            leftMargin = dip(30)
                        })
                    }
                    radioButtons[prefs.rotationMode].isChecked = true

                }.lparams(width = matchParent)

                val ballWeightIndex = weightTypesCount - prefs.ballWeight
                val weightTextView = headingTextView(
                        ctx.getString(R.string.ball_weight_text, ballWeightOptions[ballWeightIndex]))
                weightTextView.padding = dip(15)

                textView(ctx.getString(R.string.ball_weight_explanation))
                        .lparams { leftMargin = dip(10) }

                seekBar {
                    padding = dip(25)
                    max = weightTypesCount
                    progress = ballWeightIndex

                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            weightTextView.text = ctx.getString(R.string.ball_weight_text, ballWeightOptions[progress])
                            prefs.ballWeight = weightTypesCount - progress
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                }

                linearLayout {
                    for (text in ballWeightOptions) {
                        textView(text) {
                            gravity = Gravity.CENTER
                        }.lparams {
                            width = matchParent
                            weight = 1f
                        }
                    }

                }.lparams(width = matchParent)

            }
        }
    }

    private fun _LinearLayout.adjustedDrawable(image: Int) = imageView {
        imageResource = image
        adjustViewBounds = true
    }.lparams(weight = 1f)

    override fun onPause() {
        super.onPause()
        prefs.rotationMode = radioButtons.map { it.isChecked }.indexOf(true)
    }
}