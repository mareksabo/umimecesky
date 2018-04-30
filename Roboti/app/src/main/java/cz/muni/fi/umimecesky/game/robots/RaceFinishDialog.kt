package cz.muni.fi.umimecesky.game.robots


import cn.refactor.lib.colordialog.PromptDialog
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.createFinishListener
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.showDialogImmersive

class RaceFinishDialog(private val raceActivity: RaceActivity) {

    private val concept = prefs.currentRobotConcept

    fun showLosingDialog() {
        val promptDialog = PromptDialog(raceActivity)
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setTitleText(raceActivity.getString(R.string.robots_won))
                .setContentText(raceActivity.getString(R.string.win_next_time))
                .setPositiveListener(R.string.ok, raceActivity.createFinishListener())
                .setCanceledOnTouchOutside(false)
        raceActivity.showDialogImmersive(promptDialog)
    }

    fun showWinningDialog() {

        val hasIncreased = concept.increaseLevel()
        val dialogText = createDialogText(hasIncreased)

        val promptDialog = PromptDialog(raceActivity)
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setTitleText(raceActivity.getString(R.string.congratulations))
                .setContentText(dialogText)
                .setPositiveListener(R.string.ok, raceActivity.createFinishListener())
                .setCanceledOnTouchOutside(false)
        raceActivity.showDialogImmersive(promptDialog)
    }

    private fun createDialogText(levelHasIncreased: Boolean): String {
        var s = ""
        if (levelHasIncreased) {
            s += raceActivity.getString(R.string.your_next_level)
            s += " ${concept.currentLevel} "
            s += raceActivity.getString(R.string.in_category)
        } else {
            s += raceActivity.getString(R.string.u_r_at_max_level)
        }
        s += " ${concept.name}."
        return s
    }
}
