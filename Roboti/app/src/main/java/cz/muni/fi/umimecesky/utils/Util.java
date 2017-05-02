package cz.muni.fi.umimecesky.utils;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class Util {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS_FILE, MODE_PRIVATE);
    }

    public static boolean probabilityTrue(double probabilityToReturnTrue) {
        return Math.random() >= 1.0 - probabilityToReturnTrue;
    }


    public static double roundBy2places(double numberToRound) {
        return (double) Math.round(numberToRound * 100) / 100;
    }

}
