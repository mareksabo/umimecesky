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

import static cz.muni.fi.umimecesky.roboti.db.DbContract.CATEGORY_TABLE;
import static cz.muni.fi.umimecesky.roboti.db.DbContract.COMMA_SEP;
import static cz.muni.fi.umimecesky.roboti.db.DbContract.CategoryColumn.CATEGORY_ID;
import static cz.muni.fi.umimecesky.roboti.db.DbContract.CategoryColumn.NAME;
import static cz.muni.fi.umimecesky.roboti.db.DbContract.DATABASE_NAME;
import static cz.muni.fi.umimecesky.roboti.db.DbContract.DATABASE_VERSION;


public class CategoryDbHelper extends SQLiteOpenHelper {


    private static final String CATEGORY_CREATE_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE + " ( " +
                    CATEGORY_ID + " INT PRIMARY KEY " + COMMA_SEP +
                    NAME + " TEXT NOT NULL " + " )";

    private static final String CATEGORY_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CATEGORY_TABLE;


    public CategoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CATEGORY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CATEGORY_DELETE_TABLE);
        onCreate(db);
    }


    public boolean addCategory(SQLiteDatabase db, long id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_ID, id);
        contentValues.put(NAME, name);
        long wordId = db.insert(CATEGORY_TABLE, null, contentValues);
        return wordId != -1;
    }

    public Category findCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_ID + " = " + id;
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

        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

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
