package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.pojo.Category
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.utils.Constant.LAST_FILLED_WORD
import cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES_EXTRA
import cz.muni.fi.umimecesky.utils.Constant.TRAINING_NEW_WORD_DELAY_MS
import cz.muni.fi.umimecesky.utils.Conversion
import cz.muni.fi.umimecesky.utils.TrainingProgressBar
import kotlinx.android.synthetic.main.activity_training.*

class TrainingActivity : BaseAbstractActivity() {

    private var explanationText: TextView? = null
    private var trainingProgressBar: TrainingProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        initUi()

        checkedCategories = intent.getSerializableExtra(TICKED_CATEGORIES_EXTRA) as List<Category>

        categoryText = findViewById(R.id.category) as TextView
        explanationText = findViewById(R.id.explanationText) as TextView

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

    private fun initUi() {
        val viewHelper = UiViewHelper(word, firstButton, secondButton)

        super.init(viewHelper)
    }


    override fun chosenWrong(button: Button) {
        setWrong(button)
        showExplanation()
        trainingProgressBar!!.incrementIncorrectAttemptsCounter()
    }

    override fun chosenCorrect(button: Button) {
        setCorrect(button)
        trainingProgressBar!!.processCorrectResult()

        button.postDelayed({
            setButtonsEnabled()
            setNewRandomWord()
        }, TRAINING_NEW_WORD_DELAY_MS.toLong())

    }

    private fun showExplanation() {
        val explanation = currentWord.explanation
        if (!explanation.isEmpty()) {
            explanationText!!.text = explanation
            explanationText!!.visibility = View.VISIBLE
        }
    }

    private fun hideExplanation() {
        explanationText!!.visibility = View.INVISIBLE
    }

    private fun setLastUsedWord() {
        val json = sharedPref.getString(LAST_FILLED_WORD, null)
        val lastWord = Gson().fromJson<FillWord>(json, FillWord::class.java)

        if (lastWord != null) {
            setWord(lastWord)
        } else {
            setNewRandomWord()
        }
    }

    private fun setNewRandomWord() {
        val word: FillWord
        if (checkedCategories.isEmpty()) {
            word = wordHelper.randomFilledWord!!
        } else {
            val categoryIDs = Conversion.convertCategoriesToIDs(checkedCategories)
            word = wordCategoryHelper.getRandomCategoryWord(categoryIDs)
        }
        Log.v("random word", word.toString())
        setWord(word)
    }

    override fun setWord(word: FillWord?) {
        super.setWord(word)

        hideExplanation()
        setCategoryName()
        setButtonsEnabled()
    }

    private fun setCategoryName() {
        val categoryId = wordCategoryHelper.getCategoryId(currentWord.id)!!
        val category = categoryHelper.findCategory(categoryId.toLong())
        categoryText.text = category?.name
    }

    override fun onPause() {
        super.onPause()
        val json = Gson().toJson(currentWord)
        sharedPref.edit().putString(LAST_FILLED_WORD, json).apply()
    }

    override fun onResume() {
        super.onResume()
    }

}
