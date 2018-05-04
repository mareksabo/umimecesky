/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.robots


import cn.refactor.lib.colordialog.PromptDialog
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.createFinishListener
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.showDialogImmersive
import cz.muni.fi.umimecesky.prefs

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
