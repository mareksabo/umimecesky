package cz.muni.fi.umimecesky.roboti.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.pojo.FillWord;

import static cz.muni.fi.umimecesky.roboti.FillWordContract.WordEntry;

public class WordDatabaseHandler extends SQLiteOpenHelper {


    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WordEntry.TABLE_NAME + " ( " +
                    WordEntry._ID + " LONG PRIMARY KEY," +
                    WordEntry.WORD_MISSING + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.WORD_FILLED + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.VARIANT1 + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.VARIANT2 + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.CORRECT_VARIANT + " INTEGER NOT NULL " + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FillWords.db";


    public WordDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean addFilledWord(FillWord fillWord) {
        return addFilledWord(fillWord.getId(), fillWord.getWordMissing(), fillWord.getWordFilled(),
                fillWord.getVariant1(), fillWord.getVariant2(), Integer.toString(fillWord.getCorrectVariant()));
    }

    public boolean addFilledWord(long id, String missingWord, String filledWord, String variant1, String variant2, String correctVariant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordEntry._ID, id);
        contentValues.put(WordEntry.WORD_MISSING, missingWord);
        contentValues.put(WordEntry.WORD_FILLED, filledWord);
        contentValues.put(WordEntry.VARIANT1, variant1);
        contentValues.put(WordEntry.VARIANT2, variant2);
        contentValues.put(WordEntry.CORRECT_VARIANT, correctVariant);
        long wordId = db.insert(WordEntry.TABLE_NAME, null, contentValues);
        db.close();
        return wordId != -1;
    }

    public Integer deleteWord(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WordEntry.TABLE_NAME,
                "id = ? ",
                new String[]{Long.toString(id)});
    }

    public List<FillWord> getAllFilledWords() {
        List<FillWord> wordList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + WordEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                wordList.add(convertCursorToFillWord(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }


    public FillWord findWord(String filledName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + WordEntry.TABLE_NAME + " WHERE "
                + WordEntry.WORD_FILLED + " = ?", new String[]{filledName});
        if (!c.moveToFirst()) {
            return null;
        }
        return convertCursorToFillWord(c);
    }

    public FillWord getRandomFilledWord() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordEntry.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return convertCursorToFillWord(cursor);

    }

    private FillWord convertCursorToFillWord(Cursor cursor) {

        return new FillWord(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Integer.parseInt(cursor.getString(5))
        );
    }
}
