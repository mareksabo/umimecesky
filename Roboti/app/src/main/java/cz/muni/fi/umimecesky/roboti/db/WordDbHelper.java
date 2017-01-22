package cz.muni.fi.umimecesky.roboti.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.muni.fi.umimecesky.roboti.pojo.FillWord;
import cz.muni.fi.umimecesky.roboti.pojo.FillWordBuilder;

import static cz.muni.fi.umimecesky.roboti.FillWordContract.WordEntry;

public class WordDbHelper extends SQLiteOpenHelper {


    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WordEntry.TABLE_NAME + " ( " +
                    WordEntry._ID + " LONG PRIMARY KEY," +
                    WordEntry.WORD_MISSING + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.WORD_FILLED + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.VARIANT1 + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.VARIANT2 + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.CORRECT_VARIANT + " INTEGER NOT NULL " + COMMA_SEP +
                    WordEntry.EXPLANATION + " TEXT NOT NULL " + COMMA_SEP +
                    WordEntry.GRADE + " INTEGER NOT NULL " + COMMA_SEP +
                    WordEntry.IS_VISIBLE + " INTEGER NOT NULL " +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FillWords.db";


    public WordDbHelper(Context context) {
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

    public boolean addFilledWord(SQLiteDatabase db, long id, String missingWord, String filledWord, String variant1, String variant2, String correctVariant,
                                 String explanation, int grade, boolean visibility) { //TODO
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordEntry._ID, id);
        contentValues.put(WordEntry.WORD_MISSING, missingWord);
        contentValues.put(WordEntry.WORD_FILLED, filledWord);
        contentValues.put(WordEntry.VARIANT1, variant1);
        contentValues.put(WordEntry.VARIANT2, variant2);
        contentValues.put(WordEntry.CORRECT_VARIANT, correctVariant);
        contentValues.put(WordEntry.EXPLANATION, explanation);
        contentValues.put(WordEntry.GRADE, grade);
        contentValues.put(WordEntry.IS_VISIBLE, visibility);
        long wordId = db.insert(WordEntry.TABLE_NAME, null, contentValues);
        return wordId != -1;
    }

    public int deleteWord(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WordEntry.TABLE_NAME, "id = ? ", new String[]{Long.toString(id)});
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordEntry.TABLE_NAME
                + " WHERE " + WordEntry.IS_VISIBLE + " = " + WordEntry.VISIBLE_TRUE
                + " AND " + WordEntry.EXPLANATION + " <> '' "
                + " ORDER BY RANDOM() LIMIT 1", null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return convertCursorToFillWord(cursor);

    }

    private FillWord convertCursorToFillWord(Cursor cursor) {

        return new FillWordBuilder()
                .id(cursor.getLong(0))
                .wordMissing(cursor.getString(1))
                .wordFilled(cursor.getString(2))
                .variant1(cursor.getString(3))
                .variant2(cursor.getString(4))
                .correctVariant(cursor.getInt(5))
                .explanation(cursor.getString(6))
                .grade(cursor.getInt(7))
                .visibility(cursor.getInt(8) != 0)
                .createFillWord();
    }
}
