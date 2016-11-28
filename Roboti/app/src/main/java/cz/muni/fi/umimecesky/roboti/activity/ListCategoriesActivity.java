package cz.muni.fi.umimecesky.roboti.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.roboti.pojo.Category;

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

        dataAdapter = new CategoryAdapter(this, R.layout.category_info, categories);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);

    }

    private class CategoryAdapter extends ArrayAdapter<Category> implements CompoundButton.OnCheckedChangeListener {

        private List<Category> categories;

        private SparseBooleanArray checkStates;


        CategoryAdapter(Context context, int textViewResourceId, List<Category> categories) {
            super(context, textViewResourceId, categories);
            this.categories = new ArrayList<>();
            this.categories.addAll(categories);
            this.checkStates = new SparseBooleanArray(categories.size());
        }

        private class ViewHolder {
            CheckBox checkBox;
            TextView name;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.category_info, null);

                Log.v("category", getItem(position).toString());
                Log.v("Position", String.valueOf((position)));

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.categoryName);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Category category = categories.get(position);
            holder.checkBox.setText(category.getName());
            holder.checkBox.setChecked(checkStates.get(position, false));
            holder.checkBox.setOnCheckedChangeListener(this);
            holder.checkBox.setTag(position);

            return convertView;

        }


        boolean isChecked(int position) {
            return checkStates.get(position, false);
        }

        void setChecked(int position, boolean isChecked) {
            checkStates.put(position, isChecked);
            notifyDataSetChanged();
        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }

        void setAll(boolean isChecked) {
            for (int i = 0; i < this.getCount(); i++) {
                setChecked(i, isChecked);
            }
        }

        boolean areAllChecked() {
            for (int i = 0; i < getCount(); i++) {
                if (!isChecked(i)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkStates.put((Integer) buttonView.getTag(), isChecked);
        }

    }

    private void checkButtonClick() {


        Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                List<Category> categories = dataAdapter.categories;
                List<Category> checkedCategories = new ArrayList<>();

                for (int i = 0; i < dataAdapter.getCount(); i++) {
                    Log.d(String.valueOf(i), String.valueOf(dataAdapter.isChecked(i)));
                    if (dataAdapter.isChecked(i)) {
                        responseText.append("\n").append(categories.get(i).getName());
                        checkedCategories.add(categories.get(i));
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

                //todo: send categories
            }
        });

        Button tickAllButton = (Button) findViewById(R.id.tickAll);
        tickAllButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataAdapter.areAllChecked()) {
                    dataAdapter.setAll(false);
                } else {
                    dataAdapter.setAll(true);
                }
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
