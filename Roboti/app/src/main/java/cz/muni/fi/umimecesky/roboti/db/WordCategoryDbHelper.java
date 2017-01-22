package cz.muni.fi.umimecesky.roboti.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WordCategoryDbHelper extends SQLiteOpenHelper {


    private static final String COMMA_SEP = ",";
    private static final String TABLE_NAME = "words_categories";
    private static final String CATEGORY_ID = "category_id";
    private static final String WORD_ID = "word_id";
    private static final String _ID = WORD_ID;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    WORD_ID + " LONG PRIMARY KEY NOT NULL " + COMMA_SEP +
                    CATEGORY_ID + " INT NOT NULL " + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WordsCategories.db";


    public WordCategoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public boolean addConversion(SQLiteDatabase db, int categoryId, long wordId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_ID, categoryId);
        contentValues.put(WORD_ID, wordId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Integer deleteConversion(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                _ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    public Integer getCategoryId(long wordId) {

        String selectQuery = "SELECT " + CATEGORY_ID + " FROM " + TABLE_NAME + " WHERE "
                 + WORD_ID + " = " + wordId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int categoryId;
        if (cursor.moveToFirst()) {
             categoryId = Integer.parseInt(cursor.getString(0));
        } else {
            Log.d("Category missing", String.valueOf(wordId));
            categoryId = 46; // hotfix, because this category is not in conversion table
        }

        cursor.close();
        db.close();
        return categoryId;
    }

}
