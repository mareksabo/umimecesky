package cz.muni.fi.umimecesky.roboti.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.pojo.Bot;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;
import cz.muni.fi.umimecesky.roboti.utils.CalculateDp;
import cz.muni.fi.umimecesky.roboti.utils.Global;
import cz.muni.fi.umimecesky.roboti.utils.MoveLogic;
import cz.muni.fi.umimecesky.roboti.utils.RobotDrawable;

import static cz.muni.fi.umimecesky.roboti.utils.Constant.DARK_GREEN;
import static cz.muni.fi.umimecesky.roboti.utils.Constant.RACE_NEW_WORD_DELAY;

public class RaceActivity extends BaseAbstractActivity {

    private MoveLogic moveLogic;
    private Bot usersBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        ImageView usersRobot = (ImageView) findViewById(R.id.usersRobot);
        CalculateDp calculateDp = new CalculateDp(usersRobot, 5);
        calculateDp.setupFinishLine(findViewById(R.id.finishLine));
        Global.setCalculateDp(calculateDp);

        usersBot = new Bot(usersRobot, null); //TODO: change bot to robot, maybe add inheritance?

        setUpRobotViews();

        setWordText((TextView) findViewById(R.id.word));
        setVariant1((Button) findViewById(R.id.firstButton));
        setVariant2((Button) findViewById(R.id.secondButton));

        super.init();
        setNewRandomWord();

    }

    private void setUpRobotViews() {

        RobotDrawable robotDrawable = new RobotDrawable(this);
        ImageView usersRobot = (ImageView) findViewById(R.id.usersRobot);

        ImageView bot1 = (ImageView) findViewById(R.id.bot1);
        ImageView bot2 = (ImageView) findViewById(R.id.bot2);
        ImageView bot3 = (ImageView) findViewById(R.id.bot3);

        bot1.setImageDrawable(robotDrawable.removeRobotDrawable());
        bot2.setImageDrawable(robotDrawable.removeRobotDrawable());
        bot3.setImageDrawable(robotDrawable.removeRobotDrawable());

        moveLogic = new MoveLogic(this, usersRobot, bot1, bot2, bot3);
    }

    protected void setNewRandomWord() {
        FillWord word = getWordHelper().getRandomFilledWord();
        Log.d("random word", String.valueOf(word));
        setWord(word);
    }

    @Override
    protected void chosenCorrect(Button button) {
        button.setTextColor(DARK_GREEN);
        setButtonsDisabled();
        usersBot.moveForward();
        if (usersBot.isWinner()) {
            showWinningDialog();
        }
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled();
                setNewRandomWord();
            }
        }, RACE_NEW_WORD_DELAY);
    }

    private void showWinningDialog() {
        moveLogic.stopBots();
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.congratulations))
                .setContentText(getString(R.string.going_to_next_level))
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        RaceActivity.this.finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        RaceActivity.this.finish();
                    }
                });
        promptDialog.show();
    }

    @Override
    protected void chosenWrong(Button button) {
        button.setTextColor(Color.RED);
        button.setEnabled(false);
        usersBot.moveBackward();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moveLogic.stopBots();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
