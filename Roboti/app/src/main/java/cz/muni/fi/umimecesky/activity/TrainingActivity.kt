package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.db.helper.wordOpenHelper
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES
import cz.muni.fi.umimecesky.utils.Constant.TRAINING_NEW_WORD_DELAY_MS
import cz.muni.fi.umimecesky.utils.TrainingProgressBar
import kotlinx.android.synthetic.main.activity_training.categoryText
import kotlinx.android.synthetic.main.activity_training.explanationText
import kotlinx.android.synthetic.main.activity_training.firstButton
import kotlinx.android.synthetic.main.activity_training.secondButton
import kotlinx.android.synthetic.main.activity_training.seriesProgressBar
import kotlinx.android.synthetic.main.activity_training.word

class TrainingActivity : BaseAbstractActivity() {

    private lateinit var trainingProgressBar: TrainingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        super.initUI(word, firstButton, secondButton)

        @Suppress("UNCHECKED_CAST")
        checkedCategories = intent.getSerializableExtra(TICKED_CATEGORIES) as List<Category>

        setLastUsedWord()

        trainingProgressBar = TrainingProgressBar(this, seriesProgressBar)

        val touchListener = View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.alpha = .8f
                    return@OnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    // RELEASED
                    v.alpha = 1f
                    return@OnTouchListener false
                }
            }
            false
        }

        variant1.setOnTouchListener(touchListener)
        variant2.setOnTouchListener(touchListener)

    }


    override fun chosenWrong(button: Button) {
        setWrong(button)
        showExplanation()
        trainingProgressBar.incrementIncorrectAttemptsCounter()
    }

    override fun chosenCorrect(button: Button) {
        setCorrect(button)
        trainingProgressBar.processCorrectResult()

        button.postDelayed({
            enableButtons()
            setNewRandomWord()
        }, TRAINING_NEW_WORD_DELAY_MS.toLong())

    }

    private fun showExplanation() {
        val explanation = currentWord.explanation
        if (!explanation.isEmpty()) {
            explanationText.text = explanation
            explanationText.visibility = View.VISIBLE
        }
    }

    private fun hideExplanation() {
        explanationText.visibility = View.INVISIBLE
    }

    private fun setLastUsedWord() {
        val lastWord = prefs.lastShownWord

        if (lastWord != null) {
            setWord(lastWord)
        } else {
            setNewRandomWord()
        }
    }

    private fun setNewRandomWord() {
        val word: FillWord
        if (checkedCategories.isEmpty()) {
            word = wordOpenHelper.getRandomWord()
        } else {
            word = joinCategoryWordOpenHelper.getRandomCategoryWord(checkedCategories)
        }
        Log.v("random word", word.toString())
        setWord(word)
    }

    override fun setWord(word: FillWord?) {
        super.setWord(word)

        hideExplanation()
        setCategoryName()
        enableButtons()
    }

    private fun setCategoryName() {
        categoryText.text = joinCategoryWordOpenHelper.getCategory(currentWord).name
    }

    override fun onPause() {
        super.onPause()
        prefs.lastShownWord = currentWord
    }

}
