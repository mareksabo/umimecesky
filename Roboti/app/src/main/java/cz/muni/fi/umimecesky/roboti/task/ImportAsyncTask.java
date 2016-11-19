package cz.muni.fi.umimecesky.roboti.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.muni.fi.umimecesky.roboti.MainActivity;
import cz.muni.fi.umimecesky.roboti.db.WordDatabaseHandler;

public class ImportAsyncTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private int importedSize;

    public ImportAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        WordDatabaseHandler dbHelper = new WordDatabaseHandler(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.onUpgrade(db, 1,2);

        String fillInCSV = "doplnovacka_word.csv";
        AssetManager manager = activity.getAssets();

        //id;word;solved;variant1;variant2;correct;explanation;grade;visible

        try {
            InputStream inStream = manager.open(fillInCSV);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;
            String[] columnNames = buffer.readLine().split(";");

            db.beginTransaction();

            int insertedCount = 0;
            final int MAX_INSERT = 1_000_000;

            while ((line = buffer.readLine()) != null && insertedCount < MAX_INSERT) {
                insertedCount++;
                String[] columns = line.split(";");

                dbHelper.addFilledWord(db, Long.parseLong(columns[0].trim()),
                        columns[1].trim(),
                        columns[2].trim(),
                        columns[3].trim(),
                        columns[4].trim(),
                        columns[5].trim());
            }

            importedSize = insertedCount;

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putBoolean(MainActivity.IS_FILLED, true).apply();

        Toast.makeText(activity.getApplicationContext(), "Words inserted: " + importedSize,
                Toast.LENGTH_LONG).show();
    }

}
