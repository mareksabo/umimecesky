package cz.muni.fi.umimecesky.roboti.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.roboti.db.WordCategoryDbHelper;
import cz.muni.fi.umimecesky.roboti.db.WordDbHelper;
import cz.muni.fi.umimecesky.roboti.pojo.Category;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;

public class TrainingActivity extends AppCompatActivity {

    private WordDbHelper wordHelper;
    private CategoryDbHelper categoryHelper;
    private WordCategoryDbHelper wordCategoryHelper;

    private FillWord currentWord;
    private TextView wordText;
    private TextView categoryText;
    private Button variant1;
    private Button variant2;

    private List<FillWord> incorrectWords = new ArrayList<>();

    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DARK_GREEN = Color.parseColor("#4C924C");

    private SharedPreferences sharedPref;
    private static final String LAST_FILLED_WORD = "lastWord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        wordHelper = new WordDbHelper(this);
        categoryHelper = new CategoryDbHelper(this);
        wordCategoryHelper = new WordCategoryDbHelper(this);
        wordText = (TextView) findViewById(R.id.word);
        categoryText = (TextView) findViewById(R.id.category);
        variant1  = (Button) findViewById(R.id.firstButton);
        variant2  = (Button) findViewById(R.id.secondButton);

        setFirstWord();

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

    private void evaluateTask(int buttonNumber) {
        final Button button = buttonNumber == 0 ? variant1 : variant2;

        if (currentWord.getCorrectVariant() == buttonNumber) {
            button.setTextColor(DARK_GREEN);
            button.setEnabled(false);
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(true);
                    setNewRandomWord();
                }
            }, 500);
        } else {
            button.setTextColor(Color.RED);
            if (!incorrectWords.contains(currentWord)) {
                incorrectWords.add(currentWord);
            }
        }
    }

    private void setFirstWord() {
        String json = sharedPref.getString(LAST_FILLED_WORD, null);
        FillWord lastWord = new Gson().fromJson(json, FillWord.class);
        setWord(lastWord);
        if (currentWord == null) {
            setNewRandomWord();
        }
    }

    private void setNewRandomWord() {
        setWord(wordHelper.getRandomFilledWord());
    }

    private void setWord(FillWord word) {
        if (word == null) return;
        this.currentWord = word;
        wordText.setText(word.getWordMissing());
        variant1.setText(word.getVariant1());
        variant2.setText(word.getVariant2());
        variant1.setTextColor(DEFAULT_COLOR);
        variant2.setTextColor(DEFAULT_COLOR);
        int categoryId = wordCategoryHelper.getCategoryId(currentWord.getId());
        Category category = categoryHelper.findCategory(categoryId);
        if (category != null) categoryText.setText(category.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();

        String json = new Gson().toJson(currentWord);
        sharedPref.edit().putString(LAST_FILLED_WORD, json).apply();
    }
}
