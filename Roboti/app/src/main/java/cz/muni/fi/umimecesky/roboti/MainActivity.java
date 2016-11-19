package cz.muni.fi.umimecesky.roboti;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.muni.fi.umimecesky.roboti.db.WordDatabaseHandler;

public class MainActivity extends AppCompatActivity {

    private Button trainingButton;
    private Button importButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

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
                MainActivity.this.importWords();
            }
        });

    }

    public void importWords() {

        WordDatabaseHandler dbHelper = new WordDatabaseHandler(MainActivity.this.getBaseContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1,2);

        String fillInCSV = "doplnovacka_word.csv";
        AssetManager manager = getApplicationContext().getAssets();

        //id;word;solved;variant1;variant2;correct;explanation;grade;visible

        try {
            InputStream inStream = manager.open(fillInCSV);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;
            String[] columnNames = buffer.readLine().split(";");

//            db.beginTransaction();

            int i = 0;
            final int MAX_INSERT = 100;

            while ((line = buffer.readLine()) != null && i < MAX_INSERT) {
                i++;
                String[] columns = line.split(";");

                Log.d("Word: ", columns[1]);

                boolean isInserted = dbHelper.addFilledWord(Long.parseLong(columns[0].trim() ),
                        columns[1].trim(),
                        columns[2].trim(),
                        columns[3].trim(),
                        columns[4].trim(),
                        columns[5].trim());

            }


            Log.d("getAllFilledWords()", String.valueOf(dbHelper.getAllFilledWords().size()));

            Toast.makeText(MainActivity.this.getApplicationContext(), "Words inserted " + i +
                    ", total number is " + dbHelper.getAllFilledWords().size(), Toast.LENGTH_LONG).show();
//            db.setTransactionSuccessful();
//            db.endTransaction();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
