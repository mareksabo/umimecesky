package cz.muni.fi.umimecesky.roboti.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.pojo.Bot;

public class MoveLogic {

    private Bot usersRobot; //TODO: change to robot
    private Bot bot1;
    private Bot bot2;
    private Bot bot3;
    private List<Handler> handlers = new ArrayList<>();
    private Activity activity;

    public MoveLogic(Activity activity, ImageView usersRobot, ImageView botView1,
                     ImageView botView2, ImageView botView3) {
        this.activity = activity;
        this.usersRobot = new Bot(usersRobot, null);

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
        new AlertDialog.Builder(activity)
                .setTitle("Roboti vyhráli")
                .setMessage("Byl jsi poražen!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void stopBots() {
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }


    public Bot getUsersRobot() { // todo: move to activity
        return usersRobot;
    }
}
