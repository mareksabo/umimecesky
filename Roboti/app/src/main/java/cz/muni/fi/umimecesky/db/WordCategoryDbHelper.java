package cz.muni.fi.umimecesky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.muni.fi.umimecesky.pojo.FillWord;

import static cz.muni.fi.umimecesky.db.DbContract.ALL_WORD_COLUMNS;
import static cz.muni.fi.umimecesky.db.DbContract.CATEGORY_TABLE;
import static cz.muni.fi.umimecesky.db.DbContract.COMMA_SEP;
import static cz.muni.fi.umimecesky.db.DbContract.CategoryColumn.CATEGORY_ID;
import static cz.muni.fi.umimecesky.db.DbContract.DATABASE_NAME;
import static cz.muni.fi.umimecesky.db.DbContract.DATABASE_VERSION;
import static cz.muni.fi.umimecesky.db.DbContract.JOIN_TABLE;
import static cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_CATEGORY_ID;
import static cz.muni.fi.umimecesky.db.DbContract.JoinColumn.JOIN_WORD_ID;
import static cz.muni.fi.umimecesky.db.DbContract.WORD_TABLE;
import static cz.muni.fi.umimecesky.db.DbContract.WordColumn.WORD_ID;
import static cz.muni.fi.umimecesky.utils.Utils.convertCursorToFillWord;


public class WordCategoryDbHelper extends SQLiteOpenHelper {


    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + JOIN_TABLE + " ( " +
            JOIN_WORD_ID + " LONG PRIMARY KEY NOT NULL " + COMMA_SEP +
            JOIN_CATEGORY_ID + " INT NOT NULL " + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + JOIN_TABLE;

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
        contentValues.put(JOIN_CATEGORY_ID, categoryId);
        contentValues.put(JOIN_WORD_ID, wordId);
        long result = db.insert(JOIN_TABLE, null, contentValues);
        return result != -1;
    }

    public Integer getCategoryId(long wordId) {

        String selectQuery = "SELECT " + JOIN_CATEGORY_ID + " FROM " + JOIN_TABLE + " WHERE "
                + JOIN_WORD_ID + " = " + wordId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int categoryId;
        if (cursor.moveToFirst()) {
            categoryId = Integer.parseInt(cursor.getString(0));
        } else {
            Log.d("Category missing", String.valueOf(wordId));
            categoryId = -1;
        }

        cursor.close();
        db.close();
        return categoryId;
    }

    private List<FillWord> storedWords = new ArrayList<>();
    private List<Integer> storedCategories = new ArrayList<>(); // TODO: refactor variables?
    private final Random random = new Random();


    public FillWord getRandomCategoryWord(List<Integer> categoryIDs) {

        if (!storedWords.isEmpty() && !storedCategories.isEmpty() && categoryIDs.equals(storedCategories)) {
            return getRandomWord(storedWords);
        }

        String categoryIds = getCategoryIds(categoryIDs);

        final String QUERY = "SELECT " +
                ALL_WORD_COLUMNS +
                " FROM " + JOIN_TABLE + " INNER JOIN " + WORD_TABLE + " INNER JOIN " + CATEGORY_TABLE
                + " WHERE " + JOIN_TABLE + "." + JOIN_WORD_ID + " = " + WORD_TABLE + "." + WORD_ID
                + " AND " + JOIN_TABLE + "." + JOIN_CATEGORY_ID + " = " + CATEGORY_TABLE + "." + CATEGORY_ID
                + " AND " + JOIN_TABLE + "." + JOIN_CATEGORY_ID + " IN " + categoryIds;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        storedCategories = categoryIDs;
        storedWords = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            FillWord word = convertCursorToFillWord(cursor);
            storedWords.add(word);
        }

        cursor.close();
        db.close();
        return getRandomWord(storedWords);
    }

    private FillWord getRandomWord(List<FillWord> storedWords) {
        Log.i("words count", String.valueOf(storedWords.size()));
        Log.i("words", String.valueOf(storedWords));
        int index = random.nextInt(storedWords.size());
        return storedWords.remove(index);
    }


    private String getCategoryIds(List<Integer> categoryIdList) {

        StringBuilder builder = new StringBuilder("(");
        String delimiter = "";
        for (Integer integer : categoryIdList) {
            builder.append(delimiter).append(integer);
            delimiter = ",";
        }
        builder.append(")");
        return builder.toString();
    }

}
