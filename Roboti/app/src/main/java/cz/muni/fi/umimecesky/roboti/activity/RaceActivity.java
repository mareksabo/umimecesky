package cz.muni.fi.umimecesky.roboti.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.db.WordDbHelper;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;
import cz.muni.fi.umimecesky.roboti.utils.MoveLogic;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.DARK_GREEN;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.DEFAULT_COLOR;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.NEW_WORD_DELAY;

public class RaceActivity extends AppCompatActivity {

    private WordDbHelper wordHelper;

    private FillWord currentWord;
    private TextView wordText;
    private Button variant1;
    private Button variant2;

    private MoveLogic moveLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        ImageView usersRobot = (ImageView) findViewById(R.id.usersRobot);
        ImageView bot1 = (ImageView) findViewById(R.id.bot1);
        ImageView bot2 = (ImageView) findViewById(R.id.bot2);
        ImageView bot3 = (ImageView) findViewById(R.id.bot3);

        moveLogic = new MoveLogic(usersRobot, bot1, bot2, bot3);

        wordHelper = new WordDbHelper(this);
        wordText = (TextView) findViewById(R.id.word);
        variant1 = (Button) findViewById(R.id.firstButton);
        variant2 = (Button) findViewById(R.id.secondButton);

        setNewRandomWord();

        variant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateTask(0);
            }
        });
        variant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateTask(1);
            }
        });

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




    private void evaluateTask(int buttonNumber) {
        final Button button = buttonNumber == 0 ? variant1 : variant2;

        if (currentWord.getCorrectVariant() == buttonNumber) {
            chosenCorrect(button);
        } else {
            chosenWrong(button);
        }
    }

    private void chosenCorrect(Button button) {
        button.setTextColor(DARK_GREEN);
        setButtonsDisabled();
        moveLogic.getUsersRobot().moveForward();
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled();
                setNewRandomWord();
            }
        }, NEW_WORD_DELAY);
    }

    private void chosenWrong(Button button) {
        button.setTextColor(Color.RED);
        button.setEnabled(false);
        moveLogic.getUsersRobot().moveBackward();
    }

    private void setButtonsEnabled() {
        variant1.setEnabled(true);
        variant2.setEnabled(true);
    }


    private void setButtonsDisabled() {
        variant1.setEnabled(false);
        variant2.setEnabled(false);
    }

    private void setNewRandomWord() {
        FillWord word = wordHelper.getRandomFilledWord();
        Log.d("random word", String.valueOf(word));
        setWord(word);
    }

    private void setWord(FillWord word) {

        if (word == null) return;
        this.currentWord = word;
        wordText.setText(word.getWordMissing());
        variant1.setText(word.getVariant1());
        variant2.setText(word.getVariant2());
        variant1.setTextColor(DEFAULT_COLOR);
        variant2.setTextColor(DEFAULT_COLOR);

        setButtonsEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moveLogic.stopBots();
    }

}
