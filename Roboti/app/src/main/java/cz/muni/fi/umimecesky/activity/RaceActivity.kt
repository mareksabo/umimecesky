package cz.muni.fi.umimecesky.activity

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.logic.BotLogicQuick
import cz.muni.fi.umimecesky.logic.BotLogicSlow
import cz.muni.fi.umimecesky.logic.MoveLogic
import cz.muni.fi.umimecesky.pojo.Robot
import cz.muni.fi.umimecesky.pojo.RobotAnimator
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.Constant.RACE_NEW_WORD_DELAY_MS
import cz.muni.fi.umimecesky.utils.Constant.RAW_HOPS_TO_WIN
import cz.muni.fi.umimecesky.utils.RobotDrawable
import kotlinx.android.synthetic.main.activity_race.botView1
import kotlinx.android.synthetic.main.activity_race.botView2
import kotlinx.android.synthetic.main.activity_race.botView3
import kotlinx.android.synthetic.main.activity_race.finishLine
import kotlinx.android.synthetic.main.activity_race.firstButton
import kotlinx.android.synthetic.main.activity_race.secondButton
import kotlinx.android.synthetic.main.activity_race.usersRobotView
import kotlinx.android.synthetic.main.activity_race.word

// for RobotAnimator
val totalMovesToWin: Int by lazy { RaceActivity.hopsToWin }

class RaceActivity : BaseAbstractActivity() {

    companion object {
        var hopsToWin: Int = RAW_HOPS_TO_WIN + prefs.currentRobotConcept.currentLevel
        var robotMovePx: Float? = null
    }

    private lateinit var moveLogic: MoveLogic

    private val concept = prefs.currentRobotConcept
    private lateinit var usersAnimator: RobotAnimator

    private val robotWidthDp: Float
        get() = usersRobotView.layoutParams.width.toFloat().dp

    private val screenWidthDp: Float
        get() = Resources.getSystem().displayMetrics.widthPixels.toFloat().dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)
        super.initUI(word, firstButton, secondButton)

        robotMovePx = ((screenWidthDp - robotWidthDp) / totalMovesToWin).px

        setupRandomDrawables(arrayOf(botView1, botView2, botView3))
        setupFinishLine(finishLine)

        usersAnimator = RobotAnimator(usersRobotView, Robot())
        moveLogic = MoveLogic(this, createAnimators())

        setNewRandomWord()
    }


    private fun setupRandomDrawables(botViews: Array<ImageView>) {
        val robotDrawable = RobotDrawable(this)

        for (botView in botViews) {
            botView.setImageDrawable(robotDrawable.removeRobotDrawable())
        }
    }

    private fun setupFinishLine(finishLine: View) {
        val finalLineDp = screenWidthDp - robotWidthDp - 2 // 2 is lineDPThickness
        finishLine.x = finalLineDp.px
    }

    private fun createAnimators(): Array<RobotAnimator> {
        return arrayOf(
                usersAnimator,
                RobotAnimator(botView1, Robot(BotLogicQuick(concept))),
                RobotAnimator(botView2, Robot(BotLogicSlow(concept))),
                RobotAnimator(botView3, Robot(BotLogicQuick(concept)))
        )
    }

    private fun setNewRandomWord() {
        val word = joinCategoryWordOpenHelper.getRandomCategoryWord(concept)
        Log.d("random word", word.toString())
        setWord(word)
    }

    override fun chosenCorrect(button: Button) {
        setCorrect(button)
        disableButtons()
        usersAnimator.applyCorrect()
        delayNewWord(button)
    }

    private fun delayNewWord(button: Button) {
        button.postDelayed({
            enableButtons()
            setNewRandomWord()
        }, RACE_NEW_WORD_DELAY_MS.toLong())
    }

    override fun chosenWrong(button: Button) {
        setWrong(button)
        usersAnimator.applyWrong()
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

val Float.dp: Float
    get() = this / Resources.getSystem().displayMetrics.density
val Float.px: Float
    get() = this * Resources.getSystem().displayMetrics.density