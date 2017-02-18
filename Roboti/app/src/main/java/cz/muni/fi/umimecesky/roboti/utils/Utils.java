package cz.muni.fi.umimecesky.roboti.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;

import cz.muni.fi.umimecesky.roboti.pojo.FillWord;
import cz.muni.fi.umimecesky.roboti.pojo.FillWordBuilder;

import static android.content.Context.MODE_PRIVATE;

public final class Utils {


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS_FILE, MODE_PRIVATE);
    }

    /**
     * Parses string containing 0 or 1.
     *
     * @param inputString 0 or 1 in String
     * @return false if 0, true if 1
     * @throws NumberFormatException if cannot be parsed or contains other number
     */
    public static boolean stringNumberToBoolean(String inputString) throws NumberFormatException {
        int parsedInt = Integer.parseInt(inputString); // should be 1 / 0
        switch (parsedInt) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new NumberFormatException("Parsed number is neither 1 or 0, but: " + parsedInt);
        }
    }

    public static FillWord convertCursorToFillWord(Cursor cursor) {

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

    public static boolean probabilityTrue(double probabilityRatio) {
        return Math.random() >= 1.0 - probabilityRatio;
    }

    public static float dpiToPixels(float dpi) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return dpi * density;
    }

    public static float pixelsToDpi(float px) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return px / density;
    }

}
