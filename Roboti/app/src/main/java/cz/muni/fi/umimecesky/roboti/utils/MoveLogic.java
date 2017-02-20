package cz.muni.fi.umimecesky.roboti.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.pojo.Bot;

public class MoveLogic {

    private Bot bot1;
    private Bot bot2;
    private Bot bot3;
    private List<Handler> handlers = new ArrayList<>();
    private Activity activity;

    public MoveLogic(Activity activity, ImageView usersRobot, ImageView botView1,
                     ImageView botView2, ImageView botView3) {
        this.activity = activity;

        bot1 = new Bot(botView1, new BotLogicQuick());
        bot2 = new Bot(botView2, new BotLogicSlow());
        bot3 = new Bot(botView3, new BotLogicQuick());
        setupBot(bot1);
        setupBot(bot2);
        setupBot(bot3);
    }

    private void setupBot(final Bot bot) {
        final RobotLogic logic = bot.getLogic();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Log.d("x coordinate", String.valueOf(bot.getView().getX()));
                final float dpi = Utils.pixelsToDpi(bot.getView().getX());
                Log.v("dpi", String.valueOf(dpi));
                Log.v("px", String.valueOf(Utils.dpiToPixels(dpi)));
                final DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                Log.d("ApplicationTagName", "Display width in px is " + metrics.widthPixels);


                if (bot.processBotMove()) {
                    stopBots();
                    Log.d("x coordinate", String.valueOf(bot.getView().getX()));
                    Log.d("y coordinate", String.valueOf(bot.getView().getY()));
                    showLosingDialog();
                    return;
                }
                Log.d("bot", String.valueOf(logic.milisecondsPerSolution()));
                handler.postDelayed(this, logic.milisecondsPerSolution());
            }
        }, Math.round(Math.random() * 3000));
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
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
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
