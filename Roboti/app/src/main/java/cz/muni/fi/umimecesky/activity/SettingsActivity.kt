package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout.HORIZONTAL
import android.widget.RadioButton
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.prefs
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.radioButton
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.verticalLayout

/**
 * @author Marek Sabo
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var array: Array<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            padding = dip(15)

            linearLayout {

                imageView {
                    imageResource = R.drawable.phone1
                    adjustViewBounds = true
                }.lparams(weight = 1f)
                imageView {
                    imageResource = R.drawable.phone3
                    adjustViewBounds = true
                }.lparams(weight = 1f)
                imageView {
                    imageResource = R.drawable.phone2
                    adjustViewBounds = true
                }.lparams(weight = 1f)
                imageView {
                    imageResource = R.drawable.phone4
                    adjustViewBounds = true
                }.lparams(weight = 1f)
            }.lparams {
            }

            radioGroup {
                orientation = HORIZONTAL

                // TODO: fix radio button center
                val button1 = radioButton {
                    gravity = Gravity.CENTER
                }.lparams {
                    width = matchParent
                    weight = 1f
                    leftMargin =  dip(30)
                }
                val button2 = radioButton { }.lparams {
                    width = matchParent
                    weight = 1f
                    leftMargin =  dip(30)
                }
                val button3 = radioButton().lparams {
                    width = matchParent
                    weight = 1f
                    leftMargin =  dip(30)
                }
                val button4 = radioButton().lparams {
                    width = matchParent
                    weight = 1f
                    leftMargin =  dip(30)
                }
                array = arrayOf(button1, button2, button3, button4)
                array[prefs.rotationMode].isChecked = true

            }.lparams(width = matchParent)
        }
    }

    override fun onPause() {
        super.onPause()
        prefs.rotationMode = array.map { it.isChecked }.indexOf(true)
    }
}