package cz.muni.fi.umimecesky.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.activity.RaceActivity;
import cz.muni.fi.umimecesky.pojo.Bot;
import cz.muni.fi.umimecesky.pojo.RaceConcept;

public class MoveLogic {

    private List<Handler> handlers = new ArrayList<>();
    private Activity activity;

    private static final int ROBOT_START_DELAY_MS = 500;

    public MoveLogic(Activity activity, RaceConcept concept, ImageView botView1,
                     ImageView botView2, ImageView botView3) {
        this.activity = activity;

        Bot bot1 = new Bot(botView1, new BotLogicQuick(concept));
        Bot bot2 = new Bot(botView2,  new BotLogicSlow(concept));
        Bot bot3 = new Bot(botView3, new BotLogicQuick(concept));
        setupBot(bot1);
        setupBot(bot2);
        setupBot(bot3);
    }

    private void setupBot(final Bot bot) {
        final RobotLogic logic = bot.getLogic();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (bot.processBotMove()) {
                    stopBots(); // TODO disable buttons
                    showLosingDialog();
                    return;
                }
                handler.postDelayed(this, logic.millisecondsPerSolution());
            }
        }, Math.round(Math.random() * 3000 + ROBOT_START_DELAY_MS));
        handlers.add(handler);
    }

    private void showLosingDialog() {

        PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText("Roboti vyhráli")
                .setContentText("Byli jste poražen!")
                .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                activity.finish();
            }
        });
        promptDialog.setCanceledOnTouchOutside(false);
        promptDialog.show();
    }

    public void stopBots() {
        //TODO: fix by stopping first, animate second
        SystemClock.sleep(300); // have enough time to animate move forward
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
