package cz.muni.fi.umimecesky.roboti.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.task.WordImportAsyncTask;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.IS_FILLED;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        Toast.makeText(getApplicationContext(), "Start ", Toast.LENGTH_SHORT).show();
        // TODO: solve slow start

        SharedPreferences sharedPref = Utils.getSharedPreferences(this);
        setButtons();

        if (!sharedPref.getBoolean(IS_FILLED, false)) {
            new WordImportAsyncTask(MainActivity.this).execute();
        }
    }

    private void setButtons() {
        Button trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCategoriesActivity.class);
                startActivity(intent);
            }
        });

        Button importButton = (Button) findViewById(R.id.raceButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RaceActivity.class);
                startActivity(intent);
            }
        });
    }


}
