package cz.muni.fi.umimecesky.game.practise

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.google.firebase.analytics.FirebaseAnalytics
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.BaseAbstractActivity
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.game.ball.Dimensions.isTablet
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.util.Constant.TICKED_CATEGORIES
import cz.muni.fi.umimecesky.game.shared.util.Constant.TRAINING_NEW_WORD_DELAY_MS
import kotlinx.android.synthetic.main.activity_training.categoryText
import kotlinx.android.synthetic.main.activity_training.explanationText
import kotlinx.android.synthetic.main.activity_training.firstButton
import kotlinx.android.synthetic.main.activity_training.secondButton
import kotlinx.android.synthetic.main.activity_training.seriesProgressBar
import kotlinx.android.synthetic.main.activity_training.word
import org.jetbrains.anko.configuration
import org.jetbrains.anko.landscape

class TrainingActivity : BaseAbstractActivity() {

    companion object {
        private const val LAST_SEEN_WORD = "lastSeenWord"
    }

    private lateinit var trainingProgressBar: TrainingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        super.initUI(word, firstButton, secondButton)

        // TODO: make XML on small displays in landscape mode durable
        if (configuration.landscape && !isTablet())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        @Suppress("UNCHECKED_CAST")
        checkedCategories = intent.getSerializableExtra(TICKED_CATEGORIES) as List<Category>

        setNewRandomWord()

        trainingProgressBar = TrainingProgressBar(this, seriesProgressBar)

        // TODO: show better when button is pressed
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(LAST_SEEN_WORD, currentWord)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null) {
            setWord(savedInstanceState.get(LAST_SEEN_WORD) as FillWord)
        } else {
            setNewRandomWord()
        }
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
        }, TRAINING_NEW_WORD_DELAY_MS)

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

    private fun setNewRandomWord() {
        try {
            setWord(joinCategoryWordOpenHelper.getRandomCategoryWord(checkedCategories))
        } catch (e: IllegalArgumentException) {
            // TODO: remove later, just for better exception info
            val bundle = Bundle()
            bundle.putString("exception_message", e.message)
            bundle.putString("which_categories", checkedCategories.toString())
            FirebaseAnalytics.getInstance(this).logEvent("db_problem", bundle)
        }
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
}