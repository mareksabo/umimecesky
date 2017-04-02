package cz.muni.fi.umimecesky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.muni.fi.umimecesky.pojo.FillWord;

import static cz.muni.fi.umimecesky.db.DbContract.COMMA_SEP;
import static cz.muni.fi.umimecesky.db.DbContract.DATABASE_NAME;
import static cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION;
import static cz.muni.fi.umimecesky.db.DbContract.VISIBLE_TRUE;
import static cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.CORRECT_VARIANT;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.EXPLANATION;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.GRADE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.IS_VISIBLE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT1;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.VARIANT2;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_FILLED;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_MISSING;
import static cz.muni.fi.umimecesky.utils.Conversion.convertCursorToFillWord;

public class WordDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WORD_TABLE + " ( " +
                    WORD_ID + " LONG PRIMARY KEY " + COMMA_SEP +
                    WORD_MISSING + " TEXT NOT NULL " + COMMA_SEP +
                    WORD_FILLED + " TEXT NOT NULL " + COMMA_SEP +
                    VARIANT1 + " TEXT NOT NULL " + COMMA_SEP +
                    VARIANT2 + " TEXT NOT NULL " + COMMA_SEP +
                    CORRECT_VARIANT + " INTEGER NOT NULL " + COMMA_SEP +
                    EXPLANATION + " TEXT NOT NULL " + COMMA_SEP +
                    GRADE + " INTEGER NOT NULL " + COMMA_SEP +
                    IS_VISIBLE + " INTEGER NOT NULL " +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + WORD_TABLE;

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

    public boolean addFilledWord(SQLiteDatabase db, long id, String missingWord, String filledWord,
                                 String variant1, String variant2, String correctVariant,
                                 String explanation, int grade, boolean visibility) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORD_ID, id);
        contentValues.put(WORD_MISSING, missingWord);
        contentValues.put(WORD_FILLED, filledWord);
        contentValues.put(VARIANT1, variant1);
        contentValues.put(VARIANT2, variant2);
        contentValues.put(CORRECT_VARIANT, correctVariant);
        contentValues.put(EXPLANATION, explanation);
        contentValues.put(GRADE, grade);
        contentValues.put(IS_VISIBLE, visibility);
        long wordId = db.insert(WORD_TABLE, null, contentValues);
        return wordId != -1;
    }

    public FillWord findWord(String filledName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + WORD_TABLE + " WHERE "
                + WORD_FILLED + " = ?", new String[]{filledName});
        if (!c.moveToFirst()) {
            return null;
        }
        return convertCursorToFillWord(c);
    }

    public FillWord getRandomFilledWord() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WORD_TABLE
                + " WHERE " + IS_VISIBLE + " = " + VISIBLE_TRUE
                + " ORDER BY RANDOM() LIMIT 1", null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return convertCursorToFillWord(cursor);

    }


}
