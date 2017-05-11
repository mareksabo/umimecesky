package cz.muni.fi.umimecesky.logic;

import android.os.Handler;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cz.muni.fi.umimecesky.activity.RaceActivity;
import cz.muni.fi.umimecesky.pojo.Bot;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.pojo.RobotImages;
import cz.muni.fi.umimecesky.pojo.UsersRobot;
import cz.muni.fi.umimecesky.ui.RaceFinishDialog;
import cz.muni.fi.umimecesky.utils.Constant;

import static cz.muni.fi.umimecesky.pojo.AbstractRobot.noWinnerYet;

public class MoveLogic {

    private List<Handler> handlers = new ArrayList<>();
    private final RaceActivity raceActivity;

    private UsersRobot usersRobot;
    private RaceFinishDialog finishDialog;

    public MoveLogic(final RaceActivity raceActivity, RobotImages robotImages) {
        this.raceActivity = raceActivity;
        RaceConcept concept = (RaceConcept) raceActivity.getIntent().getExtras()
                .getSerializable(Constant.RACE_CONCEPT_EXTRA);
        finishDialog = new RaceFinishDialog(raceActivity, concept);
        noWinnerYet = new AtomicBoolean(true);

        Bot[] bots = createBots(robotImages.botViews, concept);
        for (Bot bot : bots) {
            setupBot(bot);
            bot.setRunnableAfter(actionWhenUserLoses());
        }

        usersRobot = new UsersRobot(robotImages.usersView);
        usersRobot.setRunnableBefore(actionWhenUserWins());
    }

    private Bot[] createBots(ImageView[] botViews, RaceConcept concept) {
        Bot bot1 = new Bot(botViews[0], new BotLogicQuick(concept));
        Bot bot2 = new Bot(botViews[1], new BotLogicSlow(concept));
        Bot bot3 = new Bot(botViews[2], new BotLogicQuick(concept));
        return new Bot[] {bot1, bot2, bot3};
    }

    private void setupBot(final Bot bot) {
        final RobotLogic logic = bot.getLogic();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                bot.processBotMove();
                if (noWinnerYet.get()) {
                    handler.postDelayed(this, logic.millisecondsPerSolution());
                }
            }
        }, (long) (Math.random() * 2000 + Constant.ROBOT_START_DELAY_MS));
        handlers.add(handler);
    }

    private Runnable actionWhenUserWins() {
        return new Runnable() {
            @Override
            public void run() {
                if (noWinnerYet.getAndSet(false)) {
                    stopBotsAndInput();
                    finishDialog.showWinningDialog();
                }
            }
        };
    }

    private Runnable actionWhenUserLoses() {
        return new Runnable() {
            @Override
            public void run() {
                if (noWinnerYet.getAndSet(false)) {
                    stopBotsAndInput();
                    finishDialog.showLosingDialog();
                }
            }
        };
    }

    public void stopBotsAndInput() {
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
        raceActivity.disableButtons();
    }

    public void applyIncorrect() {
        usersRobot.applyWrong();
    }

    public boolean applyCorrect() {
        usersRobot.applyCorrect();
        return true;
    }

}
