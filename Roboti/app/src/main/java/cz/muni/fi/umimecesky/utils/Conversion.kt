package cz.muni.fi.umimecesky.utils

import android.database.Cursor
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.pojo.FillWordBuilder

/**
 * Class representing static conversion methods.
 */

object Conversion {
    /**
     * Parses string containing 0 or 1.

     * @param inputString 0 or 1 in String
     * *
     * @return false if 0, true if 1
     * *
     * @throws NumberFormatException if cannot be parsed or contains other number
     */
    @Throws(NumberFormatException::class)
    fun stringNumberToBoolean(inputString: String): Boolean {
        val parsedInt = Integer.parseInt(inputString) // should be 1 / 0
        return when (parsedInt) {
            0 -> false
            1 -> true
            else -> throw NumberFormatException("Parsed number is neither 1 or 0, but: " + parsedInt)
        }
    }

    fun convertCursorToFillWord(cursor: Cursor): FillWord {

        return FillWordBuilder()
                .id(cursor.getLong(0))
                .wordMissing(cursor.getString(1))
                .wordFilled(cursor.getString(2))
                .variant1(cursor.getString(3))
                .variant2(cursor.getString(4))
                .correctVariant(cursor.getInt(5))
                .explanation(cursor.getString(6))
                .grade(cursor.getInt(7))
                .visibility(cursor.getInt(8) != 0)
                .createFillWord()
    }

}
