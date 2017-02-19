package cz.muni.fi.umimecesky.roboti.utils;

public class Global {
    private static Global instance;

    private static CalculateDp calculateDp;

    private Global() {
    }

    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static void setCalculateDp(CalculateDp calculateDp) {
        Global.calculateDp = calculateDp;
    }

    public static CalculateDp getCalculateDp() {
        return calculateDp;
    }
}
