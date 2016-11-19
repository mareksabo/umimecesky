package cz.muni.fi.umimecesky.roboti;

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

import cz.muni.fi.umimecesky.roboti.db.WordDatabaseHandler;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;

public class TrainingActivity extends AppCompatActivity {

    private WordDatabaseHandler handler;

    private FillWord currentWord;
    private TextView textWord;
    private Button variant1;
    private Button variant2;

    private List<FillWord> incorrectWords = new ArrayList<>();

    private static final int DEFAULT_COLOR = Color.BLACK;

    private SharedPreferences sharedPref;
    private static final String LAST_FILLED_WORD = "lastWord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        handler = new WordDatabaseHandler(this);
        textWord = (TextView) findViewById(R.id.word);
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
        Button button = buttonNumber == 0 ? variant1 : variant2;

        if (currentWord.getCorrectVariant() == buttonNumber) {
            setNewRandomWord();
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
        setWord(handler.getRandomFilledWord());
    }

    private void setWord(FillWord word) {
        this.currentWord = word;
        textWord.setText(word.getWordMissing());
        variant1.setText(word.getVariant1());
        variant2.setText(word.getVariant2());
        variant1.setTextColor(DEFAULT_COLOR);
        variant2.setTextColor(DEFAULT_COLOR);
    }

    @Override
    protected void onPause() {
        super.onPause();

        String json = new Gson().toJson(currentWord);
        sharedPref.edit().putString(LAST_FILLED_WORD, json).apply();
    }
}
