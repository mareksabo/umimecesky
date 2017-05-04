package cz.muni.fi.umimecesky.logic;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.activity.RaceActivity;
import cz.muni.fi.umimecesky.pojo.Bot;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.pojo.UsersRobot;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.GuiUtil;

public class MoveLogic {

    private List<Handler> handlers = new ArrayList<>();
    private RaceActivity raceActivity;

    private UsersRobot usersRobot;

    public MoveLogic(RaceActivity raceActivity, Bot[] bots, UsersRobot usersRobot) {
        this.raceActivity = raceActivity;
        this.usersRobot = usersRobot;

        for (Bot bot : bots) {
            setupBot(bot);
        }
    }

    private void setupBot(final Bot bot) {
        final RobotLogic logic = bot.getLogic();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                boolean shouldContinue = moveAndEvaluate(bot);
                Log.d("robot logic", logic.toString());
                Log.v("handlers", String.valueOf(handlers));
                if (shouldContinue) {
                    boolean hasRun = handler.postDelayed(this, logic.millisecondsPerSolution());
                    if (!hasRun) Log.e("handler", handler.obtainMessage() + "couldnt start");
                }
            }
        }, (long) (Math.random() * 2000 + Constant.ROBOT_START_DELAY_MS));
        handlers.add(handler);
    }

    private boolean moveAndEvaluate(Bot bot) {
        if (bot.processBotMove()) {
            stopBots();
            raceActivity.disableButtons();
            showLosingDialog();
            return false;
        }
        return true;
    }

    public void stopBots() {
        //TODO: fix by stopping first, animate second
        SystemClock.sleep(300); // have enough time to animate move forward
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void showLosingDialog() {
        PromptDialog promptDialog = new PromptDialog(raceActivity);
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setTitleText(raceActivity.getString(R.string.robots_won))
                .setContentText(raceActivity.getString(R.string.win_next_time))
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false);
        GuiUtil.showDialogImmersive(promptDialog, raceActivity);
    }


    public boolean applyCorrect(RaceConcept concept) {
        usersRobot.moveForward();
        if (usersRobot.isWinner()) {
            applyWin(concept);
            return false;
        }
        return true;
    }

    private void applyWin(RaceConcept concept) {
        stopBots();
        boolean hasIncreased = concept.increaseLevel(raceActivity);
        String dialogText = createDialogText(hasIncreased, concept);
        showWinningDialog(dialogText);
    }

    private void showWinningDialog(String dialogText) {
        final PromptDialog promptDialog = new PromptDialog(raceActivity);
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setTitleText(raceActivity.getString(R.string.congratulations))
                .setContentText(dialogText)
                .setPositiveListener(R.string.ok, GuiUtil.createFinishListener(raceActivity))
                .setCanceledOnTouchOutside(false);
        GuiUtil.showDialogImmersive(promptDialog, raceActivity);
    }

    private String createDialogText(boolean levelHasIncreased, RaceConcept concept) {
        StringBuilder s = new StringBuilder();
        if (levelHasIncreased) {
            s.append(raceActivity.getString(R.string.your_next_level));
            s.append(concept.getCurrentLevel());
            s.append(raceActivity.getString(R.string.in_category));
            s.append(concept.getName());
        } else {
            s.append(raceActivity.getString(R.string.u_r_at_max_level));
            s.append(concept.getName());
        }
        s.append(".");
        return s.toString();
    }

    public void applyIncorrect() {
        usersRobot.moveBackward();
    }
}
