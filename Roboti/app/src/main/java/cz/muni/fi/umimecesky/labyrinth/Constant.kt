package cz.muni.fi.umimecesky.labyrinth

import android.content.res.Resources

/**
 * @author Marek Sabo
 */
object Constant {

    private val metrics = Resources.getSystem().displayMetrics
    private val displayWidth = metrics.widthPixels
    private val displayHeight = metrics.heightPixels

    val metersToPixels = Point2Df(
            metrics.xdpi / 0.0254f,
            metrics.ydpi / 0.0254f)


    val ballSize = 150
    val holeSize = ballSize
    val holeRadius = holeSize / 2
    val ballRadius = ballSize / 2

    val minBallPosition = Point2Df(0f, 0f)
    val maxBallPosition = Point2Df(
            displayWidth - ballSize.toFloat(),
            displayHeight - ballSize.toFloat()
    )

    val minHolePosition = Point2Df(0f, 0f)

    val minHoleRadiusPosition = Point2Df(holeRadius.toFloat(), holeRadius.toFloat())

    val maxHoleRadiusPosition = Point2Df(
            displayWidth - holeRadius.toFloat(),
            displayHeight - holeRadius.toFloat()
    )

    val maxHolePosition = Point2Df(
            displayWidth - holeSize.toFloat(),
            displayHeight - holeSize.toFloat()
    )


}