package cz.muni.fi.umimecesky.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * Class calculates image sizes, animation moving sizes, everything in DP.
 */
public class CalculateDp {

    public float calculateRobotMovePx() {
        return CalculateDp.dpiToPixels(calculateRobotMoveDp());
    }

    private final DisplayMetrics metrics;
    private ImageView robot;
    private int winMovesCount;

    public CalculateDp(ImageView robot, int winMovesCount) {
        this.robot = robot;
        this.winMovesCount = winMovesCount;
        metrics = Resources.getSystem().getDisplayMetrics();
    }

    private float calculateRobotMoveDp() {
        float dpAmountToWin = getScreenWidthDp() - getRobotWidthDp();
        return dpAmountToWin / winMovesCount;
    }

    public void setupFinishLine(View finishLine) {
        final int LINE_THICKNESS_DP = 4;
        float finalLineDp = getScreenWidthDp() - getRobotWidthDp() - LINE_THICKNESS_DP / 2;
        finishLine.setX(CalculateDp.dpiToPixels(finalLineDp));
    }

    private float getRobotWidthDp() {
        return CalculateDp.pixelsToDpi(robot.getLayoutParams().width);
    }

    private float getScreenWidthDp() {
        float density  = metrics.density;
        return metrics.widthPixels / density;
    }

    public int getWinMovesCount() {
        return winMovesCount;
    }

    public static float dpiToPixels(float dpi) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return dpi * density;
    }

    public static float pixelsToDpi(float px) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return px / density;
    }
}
