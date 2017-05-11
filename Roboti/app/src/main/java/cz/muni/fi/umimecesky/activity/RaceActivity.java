package cz.muni.fi.umimecesky.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.logic.MoveLogic;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.pojo.RobotImages;
import cz.muni.fi.umimecesky.utils.CalculateDp;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.Global;
import cz.muni.fi.umimecesky.utils.GuiUtil;
import cz.muni.fi.umimecesky.utils.RobotDrawable;

import static cz.muni.fi.umimecesky.utils.Constant.RACE_NEW_WORD_DELAY_MS;
import static cz.muni.fi.umimecesky.utils.Constant.RAW_HOPS_TO_WIN;

public class RaceActivity extends BaseAbstractActivity {

    private MoveLogic moveLogic;

    private RaceConcept concept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        initUi();

        concept = (RaceConcept) getIntent().getExtras().getSerializable(Constant.RACE_CONCEPT_EXTRA);

        ImageView usersRobotView = (ImageView) findViewById(R.id.usersRobot);
        createRaceTrack(usersRobotView);

        setupRobotViews(usersRobotView);

        setNewRandomWord();
    }

    private void initUi() {
        UiViewHelper viewHelper = new UiViewHelper();
        viewHelper.wordText = (TextView) findViewById(R.id.word);
        viewHelper.variant1 = (Button) findViewById(R.id.firstButton);
        viewHelper.variant2 = (Button) findViewById(R.id.secondButton);

        super.init(viewHelper);
    }

    private void createRaceTrack(ImageView usersRobotView) {
        int hopsToWin = RAW_HOPS_TO_WIN + concept.getCurrentLevel();
        CalculateDp calculateDp = new CalculateDp(usersRobotView, hopsToWin);
        calculateDp.setupFinishLine(findViewById(R.id.finishLine));
        Global.setCalculateDp(calculateDp);
    }

    private void setupRobotViews(ImageView usersRobotView) {

        RobotImages robotImages = new RobotImages();
        robotImages.botViews = createBotViews();
        robotImages.usersView = usersRobotView;

        moveLogic = new MoveLogic(this, robotImages);
    }

    private ImageView[] createBotViews() {
        ImageView botView1 = (ImageView) findViewById(R.id.bot1);
        ImageView botView2 = (ImageView) findViewById(R.id.bot2);
        ImageView botView3 = (ImageView) findViewById(R.id.bot3);
        ImageView[] botViews = new ImageView[] {botView1, botView2, botView3};
        return setupRandomDrawables(botViews);
    }

    private ImageView[] setupRandomDrawables(ImageView[] botViews){
        final RobotDrawable robotDrawable = new RobotDrawable(this);

        for (ImageView botView : botViews) {
            botView.setImageDrawable(robotDrawable.removeRobotDrawable());
        }
        return botViews;
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
        boolean shouldContinue = moveLogic.applyCorrect();
        if (shouldContinue) {
            delayNewWord(button);
        }
    }
    
    private void delayNewWord(Button button) {
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled();
                setNewRandomWord();
            }
        }, RACE_NEW_WORD_DELAY_MS);
    }

    public void disableButtons() {
        setButtonsDisabled();
    }


    @Override
    protected void chosenWrong(Button button) {
        setWrong(button);
        moveLogic.applyIncorrect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moveLogic.stopBotsAndInput();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuiUtil.hideNavigationBar(this);
    }
}
