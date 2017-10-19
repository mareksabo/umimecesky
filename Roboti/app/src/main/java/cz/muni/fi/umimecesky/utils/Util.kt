package cz.muni.fi.umimecesky.utils


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object Util {

    fun getSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREFS_FILE, MODE_PRIVATE)

    fun probabilityTrue(probabilityToReturnTrue: Double): Boolean =
            Math.random() >= 1.0 - probabilityToReturnTrue


    fun roundBy2places(numberToRound: Double): Double =
            Math.round(numberToRound * 100).toDouble() / 100

    fun saveArray(context: Context, array: BooleanArray, arrayName: String): Boolean {
        val prefs = Util.getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in array.indices)
            editor.putBoolean(arrayName + "_" + i, array[i])
        return editor.commit()
    }

    fun loadArray(context: Context, arrayName: String): BooleanArray {
        val prefs = Util.getSharedPreferences(context)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array = BooleanArray(size)
        for (i in 0 until size)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false)
        return array
    }
}
