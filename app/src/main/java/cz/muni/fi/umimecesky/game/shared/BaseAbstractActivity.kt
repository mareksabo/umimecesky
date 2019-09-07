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

package cz.muni.fi.umimecesky.game.shared

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import cz.muni.fi.umimecesky.game.practise.Category
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.util.Constant.CORRECT_COLOR
import cz.muni.fi.umimecesky.game.shared.util.Constant.DEFAULT_COLOR
import cz.muni.fi.umimecesky.game.shared.util.Constant.STROKE_WIDTH
import cz.muni.fi.umimecesky.game.shared.util.Constant.WRONG_COLOR
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.hideNavigationBar

/**
 * Activity containing major properties needed to show words with word puzzles and its answers.
 *
 *
 * Method [.initUI] must be called in [.onCreate].
 */
abstract class BaseAbstractActivity : AppCompatActivity() {

    lateinit var currentWord: FillWord
        private set

    lateinit var checkedCategories: List<Category>

    private lateinit var wordText: TextView
    lateinit var variant1: Button
    lateinit var variant2: Button

    /**
     * Represents behaviour when incorrect button is pressed.
     * @param button button with [.getVariant1]
     */
    protected abstract fun chosenWrong(button: Button)

    /**
     * Represents behaviour when correct button is pressed.
     * @param button button with [.getVariant2]
     */
    protected abstract fun chosenCorrect(button: Button)

    protected fun initUI(wordText: TextView, variant1: Button, variant2: Button) {

        this.wordText = wordText
        this.variant1 = variant1
        this.variant2 = variant2

        initButtonClickListeners()
    }

    private fun initButtonClickListeners() {
        variant1.setOnClickListener { evaluateTask(0) }
        variant2.setOnClickListener { evaluateTask(1) }
    }

    private fun evaluateTask(buttonNumber: Int) {
        val button = if (buttonNumber == 0) variant1 else variant2

        if (currentWord.correctVariant == buttonNumber) {
            chosenCorrect(button)
        } else {
            chosenWrong(button)
        }
    }

    protected open fun setWord(word: FillWord?) {
        if (word == null) return
        currentWord = word
        wordText.text = word.wordMissing
        variant1.text = word.variant1
        variant2.text = word.variant2
        setDefaultButtonsColor()

        enableButtons()
    }

    private fun setDefaultButtonsColor() {
        variant1.setTextColor(DEFAULT_COLOR)
        variant2.setTextColor(DEFAULT_COLOR)
        (variant1.background as GradientDrawable).setStroke(STROKE_WIDTH, DEFAULT_COLOR)
        (variant2.background as GradientDrawable).setStroke(STROKE_WIDTH, DEFAULT_COLOR)
    }

    override fun onStop() {
        super.onStop()
        setDefaultButtonsColor() // to avoid colorful buttons in other activities
    }

    fun enableButtons() {
        variant1.isEnabled = true
        variant2.isEnabled = true
    }

    fun disableButtons() {
        variant1.isEnabled = false
        variant2.isEnabled = false
    }

    protected fun setWrong(button: Button) {

        button.setTextColor(Color.RED)

        val gradientDrawable = button.background as GradientDrawable
        gradientDrawable.setStroke(STROKE_WIDTH, WRONG_COLOR)
        button.isEnabled = false
    }

    protected fun setCorrect(button: Button) {
        wordText.text = currentWord.wordFilled
        button.setTextColor(CORRECT_COLOR)
        val gradientDrawable = button.background as GradientDrawable
        gradientDrawable.setStroke(STROKE_WIDTH, CORRECT_COLOR)
        button.isEnabled = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideNavigationBar()
    }

}
