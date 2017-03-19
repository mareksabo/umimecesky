package cz.muni.fi.umimecesky.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.muni.fi.umimecesky.pojo.Category;
import cz.muni.fi.umimecesky.pojo.FillWord;
import cz.muni.fi.umimecesky.pojo.FillWordBuilder;
import cz.muni.fi.umimecesky.pojo.RaceConcept;

import static android.content.Context.MODE_PRIVATE;

public final class Utils {

    private final static String JSON_CONCEPTS = "jsonConcepts";

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

    /**
     * Gets categories with saved player's level state.
     * Categories are bundled into testable groups, taken from original website (first 6 categories):
     *
     * @see <a href="https://www.umimecesky.cz/roboti">https://www.umimecesky.cz/roboti</a>
     */
    public static List<RaceConcept> getWebConcepts(Context context) {
        List<RaceConcept> concepts;

        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String jsonConcept = sharedPreferences.getString(JSON_CONCEPTS, null);

        if (jsonConcept != null) {
            concepts = new Gson().fromJson(jsonConcept, new TypeToken<List<RaceConcept>>() {
            }.getType());
        } else {
            concepts = initWebConcepts();
            setWebConcepts(context, concepts);
        }
        return concepts;
    }

    public static void updateConcept(Context context, RaceConcept concept) {
        List<RaceConcept> list = getWebConcepts(context);
        int index = list.indexOf(concept);
        list.set(index, concept);
        setWebConcepts(context, list);
    }

    private static void setWebConcepts(Context context, List<RaceConcept> concepts) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String json = new Gson().toJson(concepts);
        sharedPreferences.edit().putString(JSON_CONCEPTS, json).apply();
    }

    //TODO: add later more concepts - Zdvojené hlásky, Párové hlásky
    private static List<RaceConcept> initWebConcepts() {
        List<RaceConcept> concepts = new ArrayList<>();

        concepts.add(
                new RaceConcept("Koncovky Y/I",
                        Arrays.asList(8, 9, 10, 11, 12, 13, 14),
                        7)
        );
        concepts.add(
                new RaceConcept("Vyjmenovaná slova",
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                        7)
        );
        concepts.add(
                new RaceConcept("Přejatá slova, délka samohlásek",
                        Arrays.asList(26, 27, 28, 29),
                        7)
        );
        concepts.add(
                new RaceConcept("Psaní ě",
                        Arrays.asList(15, 16, 17, 18),
                        6)
        );
        concepts.add(
                new RaceConcept("Zkratky a typografie",
                        Arrays.asList(34, 35, 36, 37),
                        5)
        );
        concepts.add(
                new RaceConcept("Velká písmena",
                        Arrays.asList(38, 39, 40, 41, 42, 43, 44, 45),
                        7)
        );

        return concepts;
    }

    public static List<Integer> convertCategoriesToIDs(List<Category> categories) {
        List<Integer> ids = new ArrayList<>();
        for (Category category : categories) {
            ids.add(category.getId());
        }
        return ids;
    }

    public static float roundBy2places(double numberToRound) {
        return Math.round(numberToRound * 100) / 100;
    }

    /**
     * When activity is in immersive fullscreen mode, f.e. navigation bar is hidden. Dialog show
     * action will display the bar. This method is used to avoid displaying hidden bar.
     *
     * @param promptDialog dialog which is shown
     * @param activity     which activity is currently running
     */
    public static void showDialogImmersive(PromptDialog promptDialog, Activity activity) {
        promptDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        promptDialog.show();

        //Set the promptDialog to immersive
        promptDialog.getWindow().getDecorView().setSystemUiVisibility(
                activity.getWindow().getDecorView().getSystemUiVisibility());
        //Clear the not focusable flag from the window
        promptDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static void hideNavigationBar(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hides nav bar (buttons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
