package cz.muni.fi.umimecesky.roboti.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cz.muni.fi.umimecesky.roboti.db.WordDbHelper;
import cz.muni.fi.umimecesky.roboti.pojo.FillWord;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.DEFAULT_COLOR;

/**
 * Activity containing major properties needed to show words with options and answers.
 * <p>
 * Method {@link #init()} must be called in {@link #onCreate(Bundle)}.
 */
public abstract class BaseAbstractActivity extends AppCompatActivity {

    private WordDbHelper wordHelper;

    private FillWord currentWord;
    private TextView wordText;
    private TextView categoryText;

    private Button variant1;
    private Button variant2;


    protected void init() {
        initButtonClickListeners();
    }


    /**
     * Represents behaviour when incorrect button is pressed.
     * @param button button with {@link #getVariant1()}
     */
    protected abstract void chosenWrong(Button button);

    /**
     * Represents behaviour when correct button is pressed.
     * @param button button with {@link #getVariant2()}
     */
    protected abstract void chosenCorrect(Button button);

    private void initButtonClickListeners() {
        getVariant1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateTask(0);
            }
        });
        getVariant2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateTask(1);
            }
        });
    }

    private void evaluateTask(int buttonNumber) {
        final Button button = buttonNumber == 0 ? getVariant1() : getVariant2();

        if (getCurrentWord().getCorrectVariant() == buttonNumber) {
            chosenCorrect(button);
        } else {
            chosenWrong(button);
        }
    }

    protected void setWord(FillWord word) {
        if (word == null) return;
        setCurrentWord(word);
        getWordText().setText(word.getWordMissing());
        getVariant1().setText(word.getVariant1());
        getVariant2().setText(word.getVariant2());
        getVariant1().setTextColor(DEFAULT_COLOR);
        getVariant2().setTextColor(DEFAULT_COLOR);

        setButtonsEnabled();
    }


    protected void setButtonsEnabled() {
        variant1.setEnabled(true);
        variant2.setEnabled(true);
    }

    protected void setButtonsDisabled() {
        variant1.setEnabled(false);
        variant2.setEnabled(false);
    }


    /// GETTERS & SETTERS ///


    public WordDbHelper getWordHelper() {
        return wordHelper;
    }

    public void setWordHelper(WordDbHelper wordHelper) {
        this.wordHelper = wordHelper;
    }

    public FillWord getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(FillWord currentWord) {
        this.currentWord = currentWord;
    }

    public TextView getWordText() {
        return wordText;
    }

    public void setWordText(TextView wordText) {
        this.wordText = wordText;
    }

    public TextView getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(TextView categoryText) {
        this.categoryText = categoryText;
    }
    public Button getVariant1() {
        return variant1;
    }

    public void setVariant1(Button variant1) {
        this.variant1 = variant1;
    }

    public Button getVariant2() {
        return variant2;
    }

    public void setVariant2(Button variant2) {
        this.variant2 = variant2;
    }



}
