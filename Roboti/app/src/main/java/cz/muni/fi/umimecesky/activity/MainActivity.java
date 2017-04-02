package cz.muni.fi.umimecesky.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cz.muni.fi.umimecesky.R;
import cz.muni.fi.umimecesky.task.WordImportAsyncTask;
import cz.muni.fi.umimecesky.utils.Utils;

import static cz.muni.fi.umimecesky.utils.Constant.IS_FILLED;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = Utils.getSharedPreferences(this);
        setupButtons();

        if (!sharedPref.getBoolean(IS_FILLED, false)) {
            new WordImportAsyncTask(MainActivity.this).execute();
        }
    }

    private void setupButtons() {
        setupTrainingButton();
        setupRaceButton();
    }

    private void setupTrainingButton() {
        Button trainingButton = (Button) findViewById(R.id.trainingButton);
        Utils.setDefaultColor(trainingButton);
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCategoriesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRaceButton() {
        Button raceButton = (Button) findViewById(R.id.raceButton);
        raceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LevelRaceActivity.class);
                startActivity(intent);
            }
        });

        Utils.setDefaultColor(raceButton);
    }

}
