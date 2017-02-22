package cz.muni.fi.umimecesky.adapterlistener;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.pojo.RaceConcept;

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
            holder.currentLevel = (TextView) convertView.findViewById(R.id.currentLevel);
            holder.progressBar = (RoundCornerProgressBar) convertView.findViewById(R.id.levelProgress);
            holder.progressBar.setMax(list.get(position).getNumberOfLevels() - 1);
            Log.i("max bar", String.valueOf(holder.progressBar.getMax()));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RaceConcept raceConcept = list.get(position);
        Log.d("race concept", raceConcept + "");
        holder.sectionName.setText(raceConcept.getName());
        setProgressBar(holder, raceConcept);
        return convertView;
    }

    private void setProgressBar(ViewHolder holder, RaceConcept raceConcept) {
        RoundCornerProgressBar progressBar = holder.progressBar;
        TextView textCurrentLevel = holder.currentLevel;
        int currentLevel = raceConcept.getCurrentLevel();
        textCurrentLevel.setText(String.valueOf(currentLevel));
        currentLevel--; //progress bar starts at 0, level at 1
        progressBar.setProgress(currentLevel);
        progressBar.setSecondaryProgress(currentLevel + 1);
    }

    private class ViewHolder {
        TextView sectionName;
        TextView currentLevel;
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

