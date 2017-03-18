package cz.muni.fi.umimecesky.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.pojo.Category;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.utils.Utils;

import static cz.muni.fi.umimecesky.utils.Constant.LAST_FILLED_WORD;
import static cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES_EXTRA;
import static cz.muni.fi.umimecesky.utils.Constant.TRAINING_NEW_WORD_DELAY;

public class TrainingActivity extends BaseAbstractActivity {

    private TextView explanationText;

    @Override
    @SuppressWarnings("unchecked assignment")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        setCheckedCategories((List<Category>) getIntent().getSerializableExtra(TICKED_CATEGORIES_EXTRA));

        setWordText((TextView) findViewById(R.id.word));
        setCategoryText((TextView) findViewById(R.id.category));
        explanationText = (TextView) findViewById(R.id.explanationText);
        setVariant1((Button) findViewById(R.id.firstButton));
        setVariant2((Button) findViewById(R.id.secondButton));
        super.init();

        setLastUsedWord();


        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha(.8f);
                        return false;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        v.setAlpha(1);
                        return false;
                }
                return false;
            }
        };

        getVariant1().setOnTouchListener(touchListener);
        getVariant2().setOnTouchListener(touchListener);

    }

    @Override
    protected void chosenWrong(Button button) {
        setWrong(button);
        showExplanation();
    }

    @Override
    protected void chosenCorrect(Button button) {
        setCorrect(button);

        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled();
                setNewRandomWord();
            }
        }, TRAINING_NEW_WORD_DELAY);

    }

    private void showExplanation() {
        String explanation = getCurrentWord().getExplanation();
        if (!explanation.isEmpty()) {
            explanationText.setText(explanation);
            explanationText.setVisibility(View.VISIBLE);
        }
    }

    private void hideExplanation() {
        explanationText.setVisibility(View.INVISIBLE);
    }

    private void setLastUsedWord() {
        String json = getSharedPref().getString(LAST_FILLED_WORD, null);
        FillWord lastWord = new Gson().fromJson(json, FillWord.class);

        if (lastWord != null) {
            setWord(lastWord);
        } else {
            setNewRandomWord();
        }
    }

    private void setNewRandomWord() {
        FillWord word;
        if (getCheckedCategories() == null || getCheckedCategories().isEmpty()) {
            word = getWordHelper().getRandomFilledWord();
        } else {
            List<Integer> categoryIDs = Utils.convertCategoriesToIDs(getCheckedCategories());
            word = getWordCategoryHelper().getRandomCategoryWord(categoryIDs);
        }
        Log.v("random word", String.valueOf(word));
        setWord(word);
    }

    @Override
    protected void setWord(FillWord word) {
        super.setWord(word);

        hideExplanation();
        setCategoryName();
        setButtonsEnabled();
    }

    private void setCategoryName() {
        int categoryId = getWordCategoryHelper().getCategoryId(getCurrentWord().getId());
        Category category = getCategoryHelper().findCategory(categoryId);
        if (category != null) getCategoryText().setText(category.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        String json = new Gson().toJson(getCurrentWord());
        getSharedPref().edit().putString(LAST_FILLED_WORD, json).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hides nav bar (buttons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

}
