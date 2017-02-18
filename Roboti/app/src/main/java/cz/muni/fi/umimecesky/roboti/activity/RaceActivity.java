package cz.muni.fi.umimecesky.roboti.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;
import cz.muni.fi.umimecesky.roboti.utils.MoveLogic;
import cz.muni.fi.umimecesky.roboti.utils.RobotDrawable;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.DARK_GREEN;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.RACE_NEW_WORD_DELAY;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.ROBOT_MOVE;

public class RaceActivity extends BaseAbstractActivity {


    private MoveLogic moveLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        View view = findViewById(R.id.finishLine);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float finalLine = metrics.widthPixels -200 -ROBOT_MOVE;//-usersRobot.getDrawable().;
        Log.e("finalLine", String.valueOf(finalLine));
        view.setX(finalLine);

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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int flags =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hides status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hides nav bar (buttons)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void chosenCorrect(Button button) {
        button.setTextColor(DARK_GREEN);
        setButtonsDisabled();
        moveLogic.getUsersRobot().moveForward();
        if (moveLogic.getUsersRobot().isWinner()) {
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
        Toast.makeText(this, "Vyhral si!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void chosenWrong(Button button) {
        button.setTextColor(Color.RED);
        button.setEnabled(false);
        moveLogic.getUsersRobot().moveBackward();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moveLogic.stopBots();
    }


}
