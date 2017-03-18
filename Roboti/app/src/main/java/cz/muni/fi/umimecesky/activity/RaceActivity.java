package cz.muni.fi.umimecesky.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.pojo.Bot;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.utils.CalculateDp;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.Global;
import cz.muni.fi.umimecesky.utils.MoveLogic;
import cz.muni.fi.umimecesky.utils.RobotDrawable;
import cz.muni.fi.umimecesky.utils.Utils;

import static cz.muni.fi.umimecesky.utils.Constant.RACE_NEW_WORD_DELAY;
import static cz.muni.fi.umimecesky.utils.Constant.RAW_HOPS_TO_WIN;

public class RaceActivity extends BaseAbstractActivity {

    private MoveLogic moveLogic;
    private Bot usersBot;

    private RaceConcept concept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        concept = (RaceConcept) getIntent().getExtras().getSerializable(Constant.RACE_CONCEPT_EXTRA);

        ImageView usersRobot = (ImageView) findViewById(R.id.usersRobot);
        int hopsToWin = RAW_HOPS_TO_WIN + concept.getCurrentLevel();
        CalculateDp calculateDp = new CalculateDp(usersRobot, hopsToWin);
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

        moveLogic = new MoveLogic(this, concept, bot1, bot2, bot3);
    }

    protected void setNewRandomWord() {
        FillWord word = getWordCategoryHelper().getRandomCategoryWord(concept.getCategoryIDs());
        Log.d("random word", String.valueOf(word));
        setWord(word);
    }

    @Override
    protected void chosenCorrect(Button button) {
        setCorrect(button);
        setButtonsDisabled();
        usersBot.moveForward();
        if (usersBot.isWinner()) {
            applyWin();
            return;
        }
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled();
                setNewRandomWord();
            }
        }, RACE_NEW_WORD_DELAY);
    }

    private void applyWin() {
        moveLogic.stopBots();
        concept.increaseLevel(this);
        showWinningDialog();
    }

    private void showWinningDialog() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(false)
                .setTitleText(getString(R.string.congratulations))
                .setContentText(createDialogText())
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        RaceActivity.this.finish();
                    }
                });
        promptDialog.setCanceledOnTouchOutside(false);
        Utils.showDialogImmersive(promptDialog, this);
    }

    private String createDialogText() {
        StringBuilder s = new StringBuilder();
        if (concept.getCurrentLevel() <= concept.getNumberOfLevels()) {
            s.append("Jdete do levelu číslo ");
            s.append(concept.getCurrentLevel());
            s.append(" v kategorii ");
            s.append(concept.getName());
        } else {
            s.append("Dosáhli jste maximálního levelu v kategorii ");
            s.append(concept.getName());
        }
        s.append(".");
        return s.toString();
    }

    @Override
    protected void chosenWrong(Button button) {
        setWrong(button);
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

    @Override
    protected void onResume() {
        super.onResume();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
