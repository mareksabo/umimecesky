package cz.muni.fi.umimecesky.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.adapterlistener.LevelAdapter;
import cz.muni.fi.umimecesky.pojo.RaceConcept;
import cz.muni.fi.umimecesky.utils.Constant;
import cz.muni.fi.umimecesky.utils.GuiUtil;
import cz.muni.fi.umimecesky.utils.WebUtil;

import static cz.muni.fi.umimecesky.utils.WebUtil.getWebConcepts;

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
                List<RaceConcept> conceptList = WebUtil.getWebConcepts(LevelRaceActivity.this);
                RaceConcept raceConcept = conceptList.get(position);

                Intent intent = new Intent(LevelRaceActivity.this, RaceActivity.class);
                intent.putExtra(Constant.RACE_CONCEPT_EXTRA, raceConcept);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuiUtil.hideNavigationBar(this);
    }

}
