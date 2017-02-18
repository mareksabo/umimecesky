package cz.muni.fi.umimecesky.roboti.adapterlistener;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.pojo.RaceConcept;

public class LevelAdapter extends BaseAdapter {

    private List<RaceConcept> list;
    private Activity activity;

    public LevelAdapter(Activity activity, List<RaceConcept> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.column_category_race, parent, false);

            holder = new ViewHolder();
            holder.sectionName = (TextView) convertView.findViewById(R.id.sectionName);
            holder.progressBar = (RoundCornerProgressBar) convertView.findViewById(R.id.levelProgress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RaceConcept raceConcept = list.get(position);
        Log.d("race concept", raceConcept + "");
        holder.sectionName.setText(raceConcept.getName());
        progressBarSetup(holder.progressBar,raceConcept);
        return convertView;
    }

    private void progressBarSetup(RoundCornerProgressBar progressBar, RaceConcept raceConcept) {
        int currentLevel = raceConcept.getCurrentLevel();
        progressBar.setProgress(currentLevel);
        progressBar.setSecondaryProgress(currentLevel + 1);
    }

    private String buildLevelsString(int maxLevel) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= maxLevel; i++) {
            builder.append(i).append(" ");
        }
        return builder.toString();
    }

    private class ViewHolder {
        TextView sectionName;
        RoundCornerProgressBar progressBar;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

