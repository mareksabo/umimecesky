package cz.muni.fi.umimecesky.ui;


import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.activity.RaceActivity;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.utils.GuiUtil;

public class RaceFinishDialog {

    private RaceActivity raceActivity;
    private RaceConcept concept;

    public RaceFinishDialog(RaceActivity raceActivity, RaceConcept concept) {
        this.raceActivity = raceActivity;
        this.concept = concept;
    }

    public void showLosingDialog() {
        PromptDialog promptDialog = new PromptDialog(raceActivity);
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setTitleText(raceActivity.getString(R.string.robots_won))
                .setContentText(raceActivity.getString(R.string.win_next_time))
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false);
        GuiUtil.showDialogImmersive(promptDialog, raceActivity);
    }

    public void showWinningDialog() {

        boolean hasIncreased = concept.increaseLevel(raceActivity);
        String dialogText = createDialogText(hasIncreased);

        final PromptDialog promptDialog = new PromptDialog(raceActivity);
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setTitleText(raceActivity.getString(R.string.congratulations))
                .setContentText(dialogText)
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false);
        GuiUtil.showDialogImmersive(promptDialog, raceActivity);
    }

    private String createDialogText(boolean levelHasIncreased) {
        StringBuilder s = new StringBuilder();
        if (levelHasIncreased) {
            s.append(raceActivity.getString(R.string.your_next_level));
            s.append(" ");
            s.append(concept.getCurrentLevel());
            s.append(" ");
            s.append(raceActivity.getString(R.string.in_category));
            s.append(" ");
            s.append(concept.getName());
        } else {
            s.append(raceActivity.getString(R.string.u_r_at_max_level));
            s.append(" ");
            s.append(concept.getName());
        }
        s.append(".");
        return s.toString();
    }
}
