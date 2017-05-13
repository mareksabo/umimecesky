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

    public static boolean saveArray(Context context, boolean[] array, String arrayName) {
        SharedPreferences prefs = Util.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public static boolean[] loadArray(Context context, String arrayName) {
        SharedPreferences prefs = Util.getSharedPreferences(context);
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }
}
