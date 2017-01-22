package cz.muni.fi.umimecesky.roboti.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.adapterlistener.CategoryAdapter;
import cz.muni.fi.umimecesky.roboti.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.roboti.pojo.Category;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.LAST_FILLED_WORD;
import static cz.muni.fi.umimecesky.roboti.utils.Utils.TICKED_CATEGORIES;

public class ListCategoriesActivity extends AppCompatActivity {

    private CategoryDbHelper categoryHelper;
    private CategoryAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);

        categoryHelper = new CategoryDbHelper(this);

        displayListView();
        checkButtonClick();
    }

    private void displayListView() {
        List<Category> categories = categoryHelper.getAllCategories();

        dataAdapter = new CategoryAdapter(this, this, R.layout.category_info, categories);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);
    }


    private void checkButtonClick() {

        Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();

                List<Category> selectedCategories = dataAdapter.getSelectedCategories();
                for (Category category : selectedCategories) {
                        responseText.append("\n").append(category.getName());
                }
                Toast.makeText(getApplicationContext(),responseText, Toast.LENGTH_LONG).show();

                //todo: send categories
                Intent intent = new Intent(getBaseContext(), TrainingActivity.class);
                intent.putExtra(TICKED_CATEGORIES, (Serializable) selectedCategories);

                SharedPreferences sharedPref = Utils.getSharedPreferences(getBaseContext());
                sharedPref.edit().putString(LAST_FILLED_WORD, null).apply();
                startActivity(intent);

            }
        });


        Button tickAllButton = (Button) findViewById(R.id.tickAll);
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

}
