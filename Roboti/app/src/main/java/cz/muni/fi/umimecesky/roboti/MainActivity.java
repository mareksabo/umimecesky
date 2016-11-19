package cz.muni.fi.umimecesky.roboti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cz.muni.fi.umimecesky.roboti.db.WordDatabaseHandler;
import cz.muni.fi.umimecesky.roboti.task.ImportAsyncTask;

public class MainActivity extends AppCompatActivity {

    private Button trainingButton;
    private Button importButton;

    private SharedPreferences sharedPref;
    public static final String IS_FILLED = "isFilled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        WordDatabaseHandler dbHelper = new WordDatabaseHandler(getApplicationContext());
//        Log.d("getAllFilledWords()", String.valueOf(dbHelper.getAllFilledWords().size()));
//        Log.d("random word:  ", dbHelper.getRandomFilledWord().getWordFilled());
//        Log.d("random word:  ", dbHelper.getRandomFilledWord().getWordFilled());
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });
        importButton = (Button) findViewById(R.id.raceButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment secondFragment = new CategoryFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, secondFragment);
//                transaction.addToBackStack(null);
//
//                transaction.commit();
            }
        });


        Toast.makeText(getApplicationContext(), Boolean.toString(sharedPref.getBoolean(IS_FILLED, false)),
                Toast.LENGTH_LONG).show();
        if (!sharedPref.getBoolean(IS_FILLED, false)) {
            new ImportAsyncTask(MainActivity.this).execute();
        }

    }



}
