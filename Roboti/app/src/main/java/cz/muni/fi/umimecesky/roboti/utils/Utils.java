package cz.muni.fi.umimecesky.roboti.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import static android.content.Context.MODE_PRIVATE;

public final class Utils {

    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DARK_GREEN = Color.parseColor("#4C924C");
    public static final String LAST_FILLED_WORD = "lastWord";
    public static final String TICKED_CATEGORIES = "TICKED_CATEGORIES";
    public static final String IS_FILLED = "isFilled";


    private static final String SHARED_PREFS_FILE = "shared_prefs";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE);
    }

    /**
     * Parses string containing 0 or 1.
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

}
