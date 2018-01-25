package cz.muni.fi.umimecesky.utils

import android.content.Context
import android.content.SharedPreferences
import cz.muni.fi.umimecesky.utils.Constant.IS_FILLED

/**
 * @author Marek Sabo
 */
private const val IS_FILLED = "isImported"

class Prefs (context: Context) {
    val SHARED_PREFS_FILE = Constant.SHARED_PREFS_FILE
    val prefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    var isImported: Boolean
        get() = prefs.getBoolean(IS_FILLED, false)
        set(value) = prefs.edit().putBoolean(IS_FILLED, value).apply()
}