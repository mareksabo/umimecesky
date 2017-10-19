package cz.muni.fi.umimecesky.activity

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import cz.muni.fi.umimecesky.db.CategoryDbHelper
import cz.muni.fi.umimecesky.db.WordCategoryDbHelper
import cz.muni.fi.umimecesky.db.WordDbHelper
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.utils.Constant.CORRECT_COLOR
import cz.muni.fi.umimecesky.utils.Constant.DEFAULT_COLOR
import cz.muni.fi.umimecesky.utils.Constant.STROKE_WIDTH
import cz.muni.fi.umimecesky.utils.Constant.WRONG_COLOR
import cz.muni.fi.umimecesky.utils.GuiUtil
import cz.muni.fi.umimecesky.utils.Util

/**
 * Activity containing major properties needed to show words with word puzzles and its answers.
 *
 *
 * Method [.init] must be called in [.onCreate].
 */
abstract class BaseAbstractActivity : AppCompatActivity() {


    lateinit var wordHelper: WordDbHelper
        private set
    lateinit var wordCategoryHelper: WordCategoryDbHelper
        private set
    lateinit var categoryHelper: CategoryDbHelper
        private set

    lateinit var currentWord: FillWord
        private set

    lateinit var categoryText: TextView

    lateinit var checkedCategories: List<Category>
    lateinit var sharedPref: SharedPreferences
        private set

    lateinit var wordText: TextView
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

    protected fun init(uiViewHelper: UiViewHelper) {

        wordText = uiViewHelper.wordText
        variant1 = uiViewHelper.variant1
        variant2 = uiViewHelper.variant2

        setHelpers()
        initButtonClickListeners()
    }

    private fun setHelpers() {
        wordHelper = WordDbHelper(this)
        wordCategoryHelper = WordCategoryDbHelper(this)
        categoryHelper = CategoryDbHelper(this)
        sharedPref = Util.getSharedPreferences(this)
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

        setButtonsEnabled()
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

    protected fun setButtonsEnabled() {
        variant1.isEnabled = true
        variant2.isEnabled = true
    }

    protected fun setButtonsDisabled() {
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
        if (hasFocus) GuiUtil.hideNavigationBar(this)
    }

}