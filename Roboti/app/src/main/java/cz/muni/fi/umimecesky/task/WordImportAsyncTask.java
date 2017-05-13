package cz.muni.fi.umimecesky.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import cz.muni.fi.umimecesky.db.CategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordCategoryDbHelper;
import cz.muni.fi.umimecesky.db.WordDbHelper;
import cz.muni.fi.umimecesky.utils.Conversion;
import cz.muni.fi.umimecesky.utils.Security;
import cz.muni.fi.umimecesky.utils.Util;

import static cz.muni.fi.umimecesky.utils.Constant.IS_FILLED;

public class WordImportAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private AssetManager manager;

    public WordImportAsyncTask(Activity activity) {
        this.context = activity;
        this.manager = activity.getAssets();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            importCategories();
            importConversions();
            importWords();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void importCategories() throws IOException {
        CategoryDbHelper dbHelper = new CategoryDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);

        //id;name;
        //id;name;name2; - "&ndash;" problem

        BufferedReader buffer = openFileIgnoreFirstLine("concepts.csv");

        db.beginTransaction();

        String line;

        while ((line = buffer.readLine()) != null) {
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
    }

    private BufferedReader openFileIgnoreFirstLine(String fileName) throws IOException {

        InputStream inStream = manager.open(fileName);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        buffer.readLine(); // column names

        return buffer;
    }

    /**
     * Format concept;word;.
     * concept - category id
     * word - fill word id
     */
    private void importConversions() throws IOException {
        WordCategoryDbHelper dbHelper = new WordCategoryDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);

        BufferedReader buffer = openFileIgnoreFirstLine("doplnovacka_concept_word.csv");

        db.beginTransaction();

        String line;

        while ((line = buffer.readLine()) != null) {
            String[] columns = line.split(";");

            dbHelper.addConversion(db,
                    Integer.parseInt(columns[0].trim()),
                    Long.parseLong(columns[1].trim())
            );
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * CSV file has following data :
     * <p>
     * id;word;solved;variant1;variant2;correct;explanation;grade;visible
     * correct - 0 / 1 - if correct is variant1 or variant2
     * grade - word clue complexity
     * visible - 0 / 1 - if this word can be used or not
     */
    private void importWords() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IOException {

        WordDbHelper dbHelper = new WordDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 2);

        BufferedReader buffer = Security.getCipheredReader(manager);

        String line;

        buffer.readLine(); // column names are ignored

        db.beginTransaction();

        while ((line = buffer.readLine()) != null) {
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

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SharedPreferences prefs = Util.getSharedPreferences(context);
        prefs.edit().putBoolean(IS_FILLED, true).apply();
    }

}
