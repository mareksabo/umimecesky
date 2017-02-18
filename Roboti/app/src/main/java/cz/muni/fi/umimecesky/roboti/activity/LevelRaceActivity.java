package cz.muni.fi.umimecesky.roboti.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.adapterlistener.LevelAdapter;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.getWebConcepts;

public class LevelRaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_levels);

        ListView listView = (ListView) findViewById(R.id.levelListView);

        LevelAdapter adapter = new LevelAdapter(this, getWebConcepts(this));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //TODO: increases level, just for testing
//                List<RaceConcept> raceConcepts = Utils.getWebConcepts(LevelRaceActivity.this);
//                RaceConcept raceConcept = raceConcepts.get(position);
//                raceConcept.setCurrentLevel(raceConcept.getCurrentLevel()+1);
//                setWebConcepts(LevelRaceActivity.this, raceConcepts);
//                Log.v("race concept", String.valueOf(raceConcept));
            }

        });
    }
}
