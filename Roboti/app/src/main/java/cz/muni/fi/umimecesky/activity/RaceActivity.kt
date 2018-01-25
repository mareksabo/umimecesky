package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.logic.MoveLogic
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.pojo.RobotImages
import cz.muni.fi.umimecesky.utils.CalculateDp
import cz.muni.fi.umimecesky.utils.Constant
import cz.muni.fi.umimecesky.utils.Constant.RACE_NEW_WORD_DELAY_MS
import cz.muni.fi.umimecesky.utils.Constant.RAW_HOPS_TO_WIN
import cz.muni.fi.umimecesky.utils.Global
import cz.muni.fi.umimecesky.utils.RobotDrawable
import kotlinx.android.synthetic.main.activity_race.botView1
import kotlinx.android.synthetic.main.activity_race.botView2
import kotlinx.android.synthetic.main.activity_race.botView3
import kotlinx.android.synthetic.main.activity_race.finishLine
import kotlinx.android.synthetic.main.activity_race.firstButton
import kotlinx.android.synthetic.main.activity_race.secondButton
import kotlinx.android.synthetic.main.activity_race.usersRobotView
import kotlinx.android.synthetic.main.activity_race.word

class RaceActivity : BaseAbstractActivity() {

    private lateinit var moveLogic: MoveLogic

    private lateinit var concept: RaceConcept

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)
        initUi()

        concept = intent.extras.getSerializable(Constant.RACE_CONCEPT_EXTRA) as RaceConcept

        createRaceTrack(usersRobotView)
        setupRobotViews(usersRobotView)

        setNewRandomWord()
    }

    private fun initUi() {
        val viewHelper = UiViewHelper(word, firstButton, secondButton)

        super.init(viewHelper)
    }

    private fun createRaceTrack(usersRobotView: ImageView) {
        val hopsToWin = RAW_HOPS_TO_WIN + concept.getCurrentLevel()
        val calculateDp = CalculateDp(usersRobotView, hopsToWin)
        calculateDp.setupFinishLine(finishLine)
        Global.calculateDp = calculateDp
    }

    private fun setupRobotViews(usersRobotView: ImageView) {

        val robotImages = RobotImages(usersRobotView, createBotViews())

        moveLogic = MoveLogic(this, robotImages)
    }

    private fun createBotViews(): Array<ImageView> {
        val botViews = arrayOf(botView1, botView2, botView3)
        return setupRandomDrawables(botViews)
    }

    private fun setupRandomDrawables(botViews: Array<ImageView>): Array<ImageView> {
        val robotDrawable = RobotDrawable(this)

        for (botView in botViews) {
            botView.setImageDrawable(robotDrawable.removeRobotDrawable())
        }
        return botViews
    }

    private fun setNewRandomWord() {
        val word = joinCategoryWordOpenHelper.getRandomCategoryWord(concept)
        Log.d("random word", word.toString())
        setWord(word)
    }

    override fun chosenCorrect(button: Button) {
        setCorrect(button)
        setButtonsDisabled()
        val shouldContinue = moveLogic.applyCorrect()
        if (shouldContinue) {
            delayNewWord(button)
        }
    }

    private fun delayNewWord(button: Button) {
        button.postDelayed({
            setButtonsEnabled()
            setNewRandomWord()
        }, RACE_NEW_WORD_DELAY_MS.toLong())
    }

    fun disableButtons() {
        setButtonsDisabled()
    }


    override fun chosenWrong(button: Button) {
        setWrong(button)
        moveLogic.applyIncorrect()
    }

    override fun onPause() {
        super.onPause()
        moveLogic.stopBotsAndInput()
    }

    override fun onStop() {
        super.onStop()
        this.finish()
    }

}
