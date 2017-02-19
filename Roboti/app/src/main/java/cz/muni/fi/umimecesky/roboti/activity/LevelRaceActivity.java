package cz.muni.fi.umimecesky.roboti.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.adapterlistener.LevelAdapter;
import cz.muni.fi.umimecesky.roboti.pojo.RaceConcept;
import cz.muni.fi.umimecesky.roboti.utils.Constant;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.getWebConcepts;

public class LevelRaceActivity extends AppCompatActivity {

    private LevelAdapter levelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_levels);
    }

    @Override
    protected void onStart() {
        // TODO: replace with data update - notify?
        super.onStart();
        ListView listView = (ListView) findViewById(R.id.levelListView);

        levelAdapter = new LevelAdapter(this, getWebConcepts(this));
        listView.setAdapter(levelAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                List<RaceConcept> conceptList = Utils.getWebConcepts(LevelRaceActivity.this);
                RaceConcept raceConcept = conceptList.get(position);
                Log.v("race concept", String.valueOf(raceConcept));

                Intent intent = new Intent(LevelRaceActivity.this, RaceActivity.class);
                intent.putExtra(Constant.RACE_CONCEPT_EXTRA, raceConcept);
                startActivity(intent);
            }
        });
    }
}
