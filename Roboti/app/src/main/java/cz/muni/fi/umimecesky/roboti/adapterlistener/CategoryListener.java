package cz.muni.fi.umimecesky.roboti.adapterlistener;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import cz.muni.fi.umimecesky.roboti.R;

class CategoryListener implements View.OnClickListener {

    private boolean[] checkedStates;
    private CheckBox checkBox;

    CategoryListener(boolean[] checkedStates, CheckBox checkBox) {
        this.checkedStates = checkedStates;
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
        checkedStates[position] = !checkedStates[position];
        checkBox.setChecked(checkedStates[position]);

        Log.v("checkbox" + position, String.valueOf(checkedStates[position]));
    }
}
