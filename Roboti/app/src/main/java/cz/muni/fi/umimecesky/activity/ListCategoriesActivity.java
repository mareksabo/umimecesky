package cz.muni.fi.umimecesky.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.adapterlistener.CategoryAdapter;
import cz.muni.fi.umimecesky.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.pojo.Category;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.GuiUtil;
import cz.muni.fi.umimecesky.utils.Util;

import static cz.muni.fi.umimecesky.utils.Constant.CHECKED_STATES;
import static cz.muni.fi.umimecesky.utils.Constant.LAST_FILLED_WORD;
import static cz.muni.fi.umimecesky.utils.Constant.LAST_SPINNER_VALUE;
import static cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES_EXTRA;

public class ListCategoriesActivity extends AppCompatActivity {

    private CategoryDbHelper categoryHelper;
    private CategoryAdapter dataAdapter;

    private Button tickAllButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);

        categoryHelper = new CategoryDbHelper(this);

        tickAllButton = (Button) findViewById(R.id.tickAll);

        setSpinner();
        displayListView();
        setButtonClick();
    }

    private void setSpinner() {
        List<String> spinnerValues = Constant.ROUND_POSSIBILITIES;
        String lastValue = Util.getSharedPreferences(this)
                .getString(LAST_SPINNER_VALUE, Constant.INFINITY);

        MaterialSpinner spinner = (MaterialSpinner)  findViewById(R.id.roundsSpinner);
        spinner.setItems(spinnerValues);
        spinner.setSelectedIndex(spinnerValues.indexOf(lastValue));
        spinner.setOnItemSelectedListener(createSpinnerListener());
    }

    private MaterialSpinner.OnItemSelectedListener<String> createSpinnerListener() {
        final Activity activity = ListCategoriesActivity.this;
        return new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Util.getSharedPreferences(activity).edit().putString(LAST_SPINNER_VALUE, item).apply();
                GuiUtil.hideNavigationBar(activity);
            }
        };
    }

    private void displayListView() {
        List<Category> categories = categoryHelper.getAllCategories();

        dataAdapter = new CategoryAdapter(this, this, R.layout.category_info, categories, tickAllButton);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);
    }


    private void setButtonClick() {

        Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                List<Category> selectedCategories = dataAdapter.getSelectedCategories();
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(ListCategoriesActivity.this, R.string.choose_at_least_one_category,
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getBaseContext(), TrainingActivity.class);
                intent.putExtra(TICKED_CATEGORIES_EXTRA, (Serializable) selectedCategories);

                SharedPreferences sharedPref = Util.getSharedPreferences(getBaseContext());
                sharedPref.edit().putString(LAST_FILLED_WORD, null).apply();
                startActivity(intent);

            }
        });


        tickAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allChecked = dataAdapter.areAllChecked();
                Log.v("allChecked", String.valueOf(allChecked));
                dataAdapter.setAll(!allChecked);

            }
        });

        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.saveArray(this, dataAdapter.getCheckedStates(), CHECKED_STATES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean[] statesArray = Util.loadArray(this, CHECKED_STATES);
        if (statesArray.length > 0) dataAdapter.setCheckedStates(statesArray);
        GuiUtil.hideNavigationBar(this);
    }

}
