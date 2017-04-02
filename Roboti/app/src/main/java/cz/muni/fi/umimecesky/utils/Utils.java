package cz.muni.fi.umimecesky.utils;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class Utils {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS_FILE, MODE_PRIVATE);
    }

    public static boolean probabilityTrue(double probabilityRatio) {
        return Math.random() >= 1.0 - probabilityRatio;
    }


    public static float roundBy2places(double numberToRound) {
        return Math.round(numberToRound * 100) / 100;
    }

}
