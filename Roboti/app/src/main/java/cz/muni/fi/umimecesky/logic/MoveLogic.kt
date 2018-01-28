package cz.muni.fi.umimecesky.logic

import android.os.Handler
import android.widget.ImageView
import cz.muni.fi.umimecesky.activity.RaceActivity
import cz.muni.fi.umimecesky.pojo.AbstractRobot.Companion.noWinnerYet
import cz.muni.fi.umimecesky.pojo.Bot
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.pojo.UsersRobot
import cz.muni.fi.umimecesky.ui.RaceFinishDialog
import cz.muni.fi.umimecesky.utils.Constant
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MoveLogic(private val raceActivity: RaceActivity, robotImage: ImageView, botViews: Array<ImageView>) {

    private val handlers = ArrayList<Handler>()

    private val usersRobot: UsersRobot
    private val finishDialog: RaceFinishDialog

    init {
        val concept = raceActivity.intent.extras
                .getSerializable(Constant.RACE_CONCEPT_EXTRA) as RaceConcept
        finishDialog = RaceFinishDialog(raceActivity, concept)
        noWinnerYet = AtomicBoolean(true)

        val bots = createBots(botViews, concept)
        for (bot in bots) {
            setupBot(bot)
            bot.setRunnableAfter(actionWhenUserLoses())
        }

        usersRobot = UsersRobot(robotImage)
        usersRobot.setRunnableBefore(actionWhenUserWins())
    }

    private fun createBots(botViews: Array<ImageView>, concept: RaceConcept): Array<Bot> = arrayOf(
            Bot(botViews[0], BotLogicQuick(concept)),
            Bot(botViews[1], BotLogicSlow(concept)),
            Bot(botViews[2], BotLogicQuick(concept))
    )

    private fun setupBot(bot: Bot) {
        val logic = bot.logic
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                bot.processBotMove()
                if (noWinnerYet.get()) {
                    handler.postDelayed(this, logic.millisecondsPerSolution().toLong())
                }
            }
        }, (Math.random() * 2000 + Constant.ROBOT_START_DELAY_MS).toLong())
        handlers.add(handler)
    }

    private fun actionWhenUserWins(): Runnable = Runnable {
        if (noWinnerYet.getAndSet(false)) {
            stopBotsAndInput()
            finishDialog.showWinningDialog()
        }
    }

    private fun actionWhenUserLoses(): Runnable = Runnable {
        if (noWinnerYet.getAndSet(false)) {
            stopBotsAndInput()
            finishDialog.showLosingDialog()
        }
    }

    fun stopBotsAndInput() {
        for (handler in handlers) {
            handler.removeCallbacksAndMessages(null)
        }
        raceActivity.disableButtons()
    }

    fun applyIncorrect() = usersRobot.applyWrong()

    fun applyCorrect() = usersRobot.applyCorrect()

}
