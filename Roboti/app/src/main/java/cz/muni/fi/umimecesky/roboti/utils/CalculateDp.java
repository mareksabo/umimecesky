package cz.muni.fi.umimecesky.roboti.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * Class calculates image sizes, animation moving sizes, everything in DP.
 */
public class CalculateDp {

    public float calculateRobotMovePx() {
        return Utils.dpiToPixels(calculateRobotMoveDp());
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
        finishLine.setX(Utils.dpiToPixels(finalLineDp));
    }

    private float getRobotWidthDp() {
        return Utils.pixelsToDpi(robot.getLayoutParams().width);
    }

    private float getScreenWidthDp() {
        float density  = metrics.density;
        return metrics.widthPixels / density;
    }

    public int getWinMovesCount() {
        return winMovesCount;
    }
}
