package cz.muni.fi.umimecesky.utils

import android.content.res.Resources
import android.view.View
import android.widget.ImageView

/**
 * Class calculates image sizes, animation moving sizes, everything in DP.
 */
class CalculateDp(private val robot: ImageView, val winMovesCount: Int) {

    fun calculateRobotMovePx(): Float = ((screenWidthDp - robotWidthDp) / winMovesCount).px

    fun setupFinishLine(finishLine: View) {
        val finalLineDp = screenWidthDp - robotWidthDp - 2 // 2 is lineDPThickness
        finishLine.x = finalLineDp.px
    }

    private val robotWidthDp: Float
        get() = robot.layoutParams.width.toFloat().dp

    private val screenWidthDp: Float
        get() = Resources.getSystem().displayMetrics.widthPixels.toFloat().dp
}

val Float.dp: Float
    get() = this / Resources.getSystem().displayMetrics.density
val Float.px: Float
    get() = this * Resources.getSystem().displayMetrics.density