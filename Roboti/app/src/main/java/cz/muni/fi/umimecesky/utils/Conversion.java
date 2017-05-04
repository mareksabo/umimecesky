package cz.muni.fi.umimecesky.utils;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.umimecesky.pojo.Category;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.pojo.FillWordBuilder;
import cz.muni.fi.umimecesky.pojo.RaceConcept;

/**
 * Class representing static conversion methods.
 */

public class Conversion {
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

    public static List<Integer> convertCategoriesToIDs(List<Category> categories) {
        List<Integer> ids = new ArrayList<>();
        for (Category category : categories) {
            ids.add(category.getId());
        }
        return ids;
    }

    public static List<String> conceptToNames(List<RaceConcept> conceptList) {
        List<String> names = new ArrayList<>();
        for(RaceConcept concept : conceptList) {
            names.add(concept.getName());
        }
        return names;
    }
}
