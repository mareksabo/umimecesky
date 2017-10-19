package cz.muni.fi.umimecesky.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView

/**
 * Class calculates image sizes, animation moving sizes, everything in DP.
 */
class CalculateDp(private val robot: ImageView, val winMovesCount: Int) {

    fun calculateRobotMovePx(): Float = CalculateDp.dpiToPixels(calculateRobotMoveDp())

    private val metrics: DisplayMetrics = Resources.getSystem().displayMetrics

    private fun calculateRobotMoveDp(): Float {
        val dpAmountToWin = screenWidthDp - robotWidthDp
        return dpAmountToWin / winMovesCount
    }

    fun setupFinishLine(finishLine: View) {
        val lineDPThickness = 4
        val finalLineDp = screenWidthDp - robotWidthDp - (lineDPThickness / 2).toFloat()
        finishLine.x = CalculateDp.dpiToPixels(finalLineDp)
    }

    private val robotWidthDp: Float
        get() = CalculateDp.pixelsToDpi(robot.layoutParams.width.toFloat())

    private val screenWidthDp: Float
        get() {
            val density = metrics.density
            return metrics.widthPixels / density
        }

    companion object {

        fun dpiToPixels(dpi: Float): Float {
            val density = Resources.getSystem().displayMetrics.density
            return dpi * density
        }

        fun pixelsToDpi(px: Float): Float {
            val density = Resources.getSystem().displayMetrics.density
            return px / density
        }
    }
}
