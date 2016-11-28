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

}
