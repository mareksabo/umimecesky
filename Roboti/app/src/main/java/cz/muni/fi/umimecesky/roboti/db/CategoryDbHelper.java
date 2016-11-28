package cz.muni.fi.umimecesky.roboti.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.roboti.pojo.Category;


public class CategoryDbHelper extends SQLiteOpenHelper {


    private static final String COMMA_SEP = ",";
    private static final String TABLE_NAME = "categories";
    private static final String _ID = "id";
    private static final String NAME = "name";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    _ID + " INT PRIMARY KEY " + COMMA_SEP +
                    NAME + " TEXT NOT NULL " + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Categories.db";


    public CategoryDbHelper(Context context) {
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


    public boolean addCategory(SQLiteDatabase db, long id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, id);
        contentValues.put(NAME, name);
        long wordId = db.insert(TABLE_NAME, null, contentValues);
        return wordId != -1;
    }

    public Integer deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Long.toString(id)});
    }

    public Category findCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Category category = null;
        if (cursor.moveToFirst()) {
             category = cursorToCategory(cursor);
        } else {
            Log.d("Category not found,id", String.valueOf(id));
        }

        cursor.close();
        db.close();
        return category;
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursorToCategory(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryList;
    }

    private Category cursorToCategory(Cursor cursor) {
        return new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
    }
}
