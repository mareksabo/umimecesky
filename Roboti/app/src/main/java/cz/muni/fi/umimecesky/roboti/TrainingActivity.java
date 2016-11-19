package cz.muni.fi.umimecesky.roboti;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

//    private SharedPreferences sharedPref;
    private static final String LAST_FILLED_WORD = "lastWord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        handler = new WordDatabaseHandler(this);
        textWord = (TextView) findViewById(R.id.word);
        variant1  = (Button) findViewById(R.id.firstButton);
        variant2  = (Button) findViewById(R.id.secondButton);

        setNewRandomWord();
        Log.d("current", currentWord.toString());

        variant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWord.getCorrectVariant() == 0) {
                    setNewRandomWord();
                } else {
                    if (!incorrectWords.contains(currentWord)) {
                        incorrectWords.add(currentWord);
                    }
                }
            }
        });
        variant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWord.getCorrectVariant() == 1) {
                    setNewRandomWord();
                } else {
                    if (!incorrectWords.contains(currentWord)) {
                        incorrectWords.add(currentWord);
                    }
                }
            }
        });

//        setNewRandomWord();

//        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        String lastFilled = sharedPref.getString(LAST_FILLED_WORD, null);
//        if (lastFilled != null) {
//            Log.d("lastFilled ", lastFilled );
//            currentWord = null;//handler.findWord(lastFilled);
//            Log.d("word is ", currentWord == null ? "null" : "not null");
//            if (currentWord != null) {
//                Log.d("word suc. found", currentWord.toString());
//                setWord();
//            } else {
//                setNewRandomWord();
//            }
//        } else {
//            setNewRandomWord();
//        }

    }

    private void setNewRandomWord() {
        currentWord = handler.getRandomFilledWord();
        textWord.setText(currentWord.getWordMissing());
        variant1.setText(currentWord.getVariant1());
        variant2.setText(currentWord.getVariant2());
//        sharedPref.edit().putString(LAST_FILLED_WORD, currentWord.getWordFilled()).apply();
    }

    private void setWord() {
        Log.i("Text set", "");
    }

}
