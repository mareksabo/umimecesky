package cz.muni.fi.umimecesky.adapterlistener;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import cz.muni.fi.umimecesky.R;

class CategoryListener implements View.OnClickListener {

    private CategoryAdapter categoryAdapter;
    private CheckBox checkBox;

    CategoryListener(CategoryAdapter categoryAdapter, CheckBox checkBox) {
        this.categoryAdapter = categoryAdapter;
        this.checkBox = checkBox;
    }

    @Override
    public void onClick(View v) {
        int position;

        switch (v.getId()) {
            case R.id.categoryCheckLayout:
                CategoryAdapter.ViewHolder holder = ((CategoryAdapter.ViewHolder) v.getTag());
                position = (int) holder.checkBox.getTag();
                break;
            case R.id.checkBox:
                position = (int) v.getTag();
                break;
            default:
                throw new IllegalArgumentException("Wrong class: " + v.getClass().getName());
        }
        boolean changeChecked = !categoryAdapter.isChecked(position);
        categoryAdapter.setCategoryChecked(position, changeChecked);
        checkBox.setChecked(changeChecked);

        Log.v("checkbox" + position, String.valueOf(changeChecked));
    }
}
