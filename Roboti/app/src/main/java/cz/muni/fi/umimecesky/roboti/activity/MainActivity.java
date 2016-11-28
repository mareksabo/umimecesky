package cz.muni.fi.umimecesky.roboti.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cz.muni.fi.umimecesky.roboti.R;
import cz.muni.fi.umimecesky.roboti.db.WordDbHelper;
import cz.muni.fi.umimecesky.roboti.task.WordImportAsyncTask;
import cz.muni.fi.umimecesky.roboti.utils.Utils;

import static cz.muni.fi.umimecesky.roboti.utils.Utils.IS_FILLED;

public class MainActivity extends AppCompatActivity {

    private Button trainingButton;
    private Button importButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);
        sharedPref = Utils.getSharedPreferences(this);

        WordDbHelper dbHelper = new WordDbHelper(getApplicationContext());
        trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCategoriesActivity.class);
                startActivity(intent);
            }
        });
        importButton = (Button) findViewById(R.id.raceButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainingActivity.class);
                startActivity(intent);
//                Fragment secondFragment = new CategoryFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, secondFragment);
//                transaction.addToBackStack(null);
//
//                transaction.commit();
            }
        });

        if (!sharedPref.getBoolean(IS_FILLED, false)) {
            new WordImportAsyncTask(MainActivity.this).execute();
        }

    }




}
