package cz.muni.fi.umimecesky.game.robots

import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.game.ball.Dimensions.isTablet
import cz.muni.fi.umimecesky.game.robots.RobotDrawable.getRobotDrawable
import cz.muni.fi.umimecesky.game.robots.logic.BotLogicQuick
import cz.muni.fi.umimecesky.game.robots.logic.BotLogicSlow
import cz.muni.fi.umimecesky.game.robots.logic.MoveLogic
import cz.muni.fi.umimecesky.game.shared.BaseAbstractActivity
import cz.muni.fi.umimecesky.game.shared.util.Constant.RACE_NEW_WORD_DELAY_MS
import cz.muni.fi.umimecesky.game.shared.util.Constant.RAW_HOPS_TO_WIN
import cz.muni.fi.umimecesky.prefs
import kotlinx.android.synthetic.main.activity_race.botView1
import kotlinx.android.synthetic.main.activity_race.botView2
import kotlinx.android.synthetic.main.activity_race.botView3
import kotlinx.android.synthetic.main.activity_race.finishLine
import kotlinx.android.synthetic.main.activity_race.firstButton
import kotlinx.android.synthetic.main.activity_race.secondButton
import kotlinx.android.synthetic.main.activity_race.usersRobotView
import kotlinx.android.synthetic.main.activity_race.word
import org.jetbrains.anko.configuration
import org.jetbrains.anko.landscape

// for RobotAnimator

class RaceActivity : BaseAbstractActivity() {

    private val concept = prefs.currentRobotConcept
    private val totalMovesToWin = RAW_HOPS_TO_WIN + concept.currentLevel

    private val screenWidthDp: Float
        get() = Resources.getSystem().displayMetrics.widthPixels.toFloat().dp

    private val robotWidthDp: Float
        get() = usersRobotView.layoutParams.width.toFloat().dp

    private lateinit var moveLogic: MoveLogic
    private lateinit var usersAnimator: RobotAnimator
    private var robotMovePx: Float = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)
        super.initUI(word, firstButton, secondButton)
        if (configuration.landscape && !isTablet()) supportActionBar?.hide()

        robotMovePx = ((screenWidthDp - robotWidthDp) / totalMovesToWin).px
        finishLine.x = (screenWidthDp - robotWidthDp - 2).px // 2 dp is line thickness
        setupRandomDrawables(arrayOf(botView1, botView2, botView3))

        usersAnimator = RobotAnimator(usersRobotView, Robot(), totalMovesToWin, robotMovePx)
        moveLogic = MoveLogic(this, createAnimators())

        setNewRandomWord()
    }


    private fun setupRandomDrawables(botViews: Array<ImageView>) {
        for (botView in botViews) {
            botView.setImageDrawable(getRobotDrawable(this))
        }
    }

    private fun createAnimators(): Array<RobotAnimator> {
        return arrayOf(
                usersAnimator,
                RobotAnimator(botView1, Robot(BotLogicQuick(concept)), totalMovesToWin, robotMovePx),
                RobotAnimator(botView2, Robot(BotLogicSlow(concept)), totalMovesToWin, robotMovePx),
                RobotAnimator(botView3, Robot(BotLogicQuick(concept)), totalMovesToWin, robotMovePx)
        )
    }

    private fun setNewRandomWord() =
            setWord(joinCategoryWordOpenHelper.getRandomCategoryWord(concept))

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
        }, RACE_NEW_WORD_DELAY_MS)
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