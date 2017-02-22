package cz.muni.fi.umimecesky.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.umimecesky.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordCategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordDbHelper;
import cz.muni.fi.umimecesky.pojo.Category;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.utils.Utils;

import static cz.muni.fi.umimecesky.utils.Constant.CORRECT_COLOR;
import static cz.muni.fi.umimecesky.utils.Constant.DEFAULT_COLOR;
import static cz.muni.fi.umimecesky.utils.Constant.STROKE_WIDTH;
import static cz.muni.fi.umimecesky.utils.Constant.WRONG_COLOR;

/**
 * Activity containing major properties needed to show words with word puzzles and its answers.
 * <p>
 * Method {@link #init()} must be called in {@link #onCreate(Bundle)}.
 */
public abstract class BaseAbstractActivity extends AppCompatActivity {

    private WordDbHelper wordHelper;
    private WordCategoryDbHelper wordCategoryHelper;
    private CategoryDbHelper categoryHelper;

    private FillWord currentWord;
    private TextView wordText;
    private TextView categoryText;

    private Button variant1;
    private Button variant2;

    private List<Category> checkedCategories;
    private SharedPreferences sharedPref;

    // TODO: solve init to avoid accessing null reference (f.e. variant1)
    protected void init() {
        initButtonClickListeners();
        setHelpers();
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

    private void setHelpers() {
        wordHelper = new WordDbHelper(this);
        wordCategoryHelper = new WordCategoryDbHelper(this);
        categoryHelper = new CategoryDbHelper(this);

        sharedPref = Utils.getSharedPreferences(this);
    }

    private void initButtonClickListeners() {
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
            chosenCorrect(button);
        } else {
            chosenWrong(button);
        }
    }

    protected void setWord(FillWord word) {
        if (word == null) return;
        currentWord = word;
        wordText.setText(word.getWordMissing());
        variant1.setText(word.getVariant1());
        variant2.setText(word.getVariant2());
        variant1.setTextColor(DEFAULT_COLOR);
        variant2.setTextColor(DEFAULT_COLOR);
        ((GradientDrawable) variant1.getBackground()).setStroke(STROKE_WIDTH, DEFAULT_COLOR);
        ((GradientDrawable) variant2.getBackground()).setStroke(STROKE_WIDTH, DEFAULT_COLOR);

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

    protected void setWrong(Button button) {

        button.setTextColor(Color.RED);

        GradientDrawable gradientDrawable = (GradientDrawable) button.getBackground();
        gradientDrawable.setStroke(STROKE_WIDTH, WRONG_COLOR);
        button.setEnabled(false);
    }

    protected void setCorrect(Button button) {
        getWordText().setText(getCurrentWord().getWordFilled());
        button.setTextColor(CORRECT_COLOR);
        GradientDrawable gradientDrawable = (GradientDrawable) button.getBackground();
        gradientDrawable.setStroke(STROKE_WIDTH, CORRECT_COLOR);
        button.setEnabled(false);
    }

    /// GETTERS & SETTERS ///


    public WordDbHelper getWordHelper() {
        return wordHelper;
    }

    public WordCategoryDbHelper getWordCategoryHelper() {
        return wordCategoryHelper;
    }

    public CategoryDbHelper getCategoryHelper() {
        return categoryHelper;
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public FillWord getCurrentWord() {
        return currentWord;
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


    public List<Category> getCheckedCategories() {
        return checkedCategories;
    }

    public void setCheckedCategories(List<Category> checkedCategories) {
        this.checkedCategories = checkedCategories;
    }

}
