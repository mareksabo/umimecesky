package cz.muni.fi.umimecesky.adapterlistener;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.pojo.Category;

import static cz.muni.fi.umimecesky.R.id.checkBox;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Activity activity;
    private boolean[] checkedStates;
    private List<Category> categoryList;
    private final Button tickAll;

    public CategoryAdapter(Activity activity, Context context, int textViewResourceId, List<Category> categories, Button tickAll) {
        super(context, textViewResourceId, categories);
        this.activity = activity;
        checkedStates = new boolean[categories.size()];
        categoryList = new ArrayList<>(categories);
        this.tickAll = tickAll;
    }


    class ViewHolder {
        TextView categoryName;
        CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.category_info, parent, false);

            holder = new ViewHolder();
            holder.categoryName = (TextView) convertView.findViewById(R.id.categoryName);
            holder.checkBox = (CheckBox) convertView.findViewById(checkBox);
            holder.checkBox.setTag(position);
            convertView.setTag(holder);

            CategoryListener listener = new CategoryListener(this, holder.checkBox);
            convertView.setOnClickListener(listener);
            holder.checkBox.setOnClickListener(listener);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());
        holder.checkBox.setChecked(checkedStates[position]);
        holder.checkBox.setTag(position);

        return convertView;

    }


    boolean isChecked(int position) {
        return checkedStates[position];
    }

    void setCategoryChecked(int position, boolean isChecked) {
        checkedStates[position] = isChecked;
        notifyDataSetChanged();
        checkTickAllText();
    }

    public void setAll(boolean isChecked) {
        for (int i = 0; i < this.getCount(); i++) {
            setCategoryChecked(i, isChecked);
        }
    }

    public boolean areAllChecked() {
        for (int i = 0; i < getCount(); i++) {
            if (!isChecked(i)) {
                return false;
            }
        }
        return true;
    }

    public List<Category> getSelectedCategories() {
        List<Category> selectedCategories = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            if (checkedStates[i]) {
                selectedCategories.add(categoryList.get(i));
            }
        }
        return selectedCategories;
    }

    public void checkTickAllText() {
        if (areAllChecked()) {
            tickAll.setText(R.string.none);
        } else {
            tickAll.setText(R.string.all);
        }
    }
}
