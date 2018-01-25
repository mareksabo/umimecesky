package cz.muni.fi.umimecesky.ui


import cn.refactor.lib.colordialog.PromptDialog
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.activity.RaceActivity
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.utils.GuiUtil

class RaceFinishDialog(private val raceActivity: RaceActivity, private val concept: RaceConcept) {

    fun showLosingDialog() {
        val promptDialog = PromptDialog(raceActivity)
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setTitleText(raceActivity.getString(R.string.robots_won))
                .setContentText(raceActivity.getString(R.string.win_next_time))
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false)
        GuiUtil.showDialogImmersive(promptDialog, raceActivity)
    }

    fun showWinningDialog() {

        val hasIncreased = concept.increaseLevel()
        val dialogText = createDialogText(hasIncreased)

        val promptDialog = PromptDialog(raceActivity)
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setTitleText(raceActivity.getString(R.string.congratulations))
                .setContentText(dialogText)
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false)
        GuiUtil.showDialogImmersive(promptDialog, raceActivity)
    }

    private fun createDialogText(levelHasIncreased: Boolean): String {
        val s = StringBuilder()
        if (levelHasIncreased) {
            s.append(raceActivity.getString(R.string.your_next_level))
            s.append(" ")
            s.append(concept.getCurrentLevel())
            s.append(" ")
            s.append(raceActivity.getString(R.string.in_category))
            s.append(" ")
            s.append(concept.name)
        } else {
            s.append(raceActivity.getString(R.string.u_r_at_max_level))
            s.append(" ")
            s.append(concept.name)
        }
        s.append(".")
        return s.toString()
    }
}
