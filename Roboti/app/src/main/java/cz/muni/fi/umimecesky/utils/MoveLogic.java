package cz.muni.fi.umimecesky.utils;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.activity.RaceActivity;
import cz.muni.fi.umimecesky.pojo.Bot;

public class MoveLogic {

    private List<Handler> handlers = new ArrayList<>();
    private RaceActivity raceActivity;

    private static final int ROBOT_START_DELAY_MS = 200;

    public MoveLogic(RaceActivity raceActivity, Bot[] bots) {
        this.raceActivity = raceActivity;

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
                if (shouldContinue) {
                    handler.postDelayed(this, logic.millisecondsPerSolution());
                }
            }
        }, (long) (Math.random() * 2000 + ROBOT_START_DELAY_MS));
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

    private void showLosingDialog() {
        PromptDialog promptDialog = new PromptDialog(raceActivity);
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText("Roboti vyhráli")
                .setContentText("Byl jsi poražen!")
                .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        raceActivity.finish();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                raceActivity.finish();
            }
        });
        promptDialog.setCanceledOnTouchOutside(false);
        Utils.showDialogImmersive(promptDialog, raceActivity);
    }

    public void stopBots() {
        //TODO: fix by stopping first, animate second
        SystemClock.sleep(300); // have enough time to animate move forward
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
