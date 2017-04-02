package cz.muni.fi.umimecesky.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.muni.fi.umimecesky.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordCategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordDbHelper;
import cz.muni.fi.umimecesky.utils.Conversion;
import cz.muni.fi.umimecesky.utils.Util;

import static cz.muni.fi.umimecesky.utils.Constant.IS_FILLED;

public class WordImportAsyncTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private int importedSize;

    public WordImportAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        importCategories();
        importConversions();
        importWords();

        return null;
    }

    private void importCategories() {
        CategoryDbHelper dbHelper = new CategoryDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //id;name;
        //id;name;name2; - "&ndash;" problem

        try {
            InputStream inStream = activity.getAssets().open("concepts.csv");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;
            buffer.readLine(); // column names

            db.beginTransaction();

            int insertedCount = 0;
            final int MAX_INSERT = 1_000_000;

            while ((line = buffer.readLine()) != null && insertedCount < MAX_INSERT) {
                insertedCount++;
                String[] columns = line.split(";");

                if (columns.length == 3) {
                    columns[1] = columns[1].trim() + " - " + columns[2].trim();
                }

                int id = Integer.parseInt(columns[0].trim());
                if (id == 46) continue;
                dbHelper.addCategory(db, id, columns[1].trim());
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Format concept;word;.
     * concept - category id
     * word - fill word id
     */
    private void importConversions() {
        WordCategoryDbHelper dbHelper = new WordCategoryDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);

        try {
            InputStream inStream = activity.getAssets().open("doplnovacka_concept_word.csv");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;

            buffer.readLine(); // column names

            db.beginTransaction();

            int insertedCount = 0;
            final int MAX_INSERT = 1_000_000;

            while ((line = buffer.readLine()) != null && insertedCount < MAX_INSERT) {
                insertedCount++;
                String[] columns = line.split(";");

                dbHelper.addConversion(db,
                        Integer.parseInt(columns[0].trim()),
                        Long.parseLong(columns[1].trim())
                );
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            Log.d("conversions", dbHelper.getCategoryId(1).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * CSV file has following data :
     * <p>
     * id;word;solved;variant1;variant2;correct;explanation;grade;visible
     * correct - 0 / 1 - if correct is variant1 or variant2
     * grade - word clue complexity
     * visible - 0 / 1 - if this word can be used or not
     */
    private void importWords() {
        WordDbHelper dbHelper = new WordDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);

        final String WORD_CSV_FILENAME = "doplnovacka_word.csv";
        AssetManager manager = activity.getAssets();

        try {
            InputStream inStream = manager.open(WORD_CSV_FILENAME);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;

            buffer.readLine(); // column names ignored

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
                        columns[5].trim(),
                        columns[6].trim(),
                        Integer.parseInt(columns[7].trim()),
                        Conversion.stringNumberToBoolean(columns[8].trim())
                );
            }

            importedSize = insertedCount;

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SharedPreferences prefs = Util.getSharedPreferences(activity);
        prefs.edit().putBoolean(IS_FILLED, true).apply();
    }

}
