package cz.muni.fi.umimecesky.labyrinth

import android.content.res.Resources

/**
 * @author Marek Sabo
 */
// TODO: Make as usual class with singleton. Object will be created Every time settings are changed.
object Constant {

    private fun metrics() = Resources.getSystem().displayMetrics
    private fun displayWidth() = metrics().widthPixels
    private fun displayHeight() = metrics().heightPixels

    fun metersToPixelsX() = metrics().xdpi / 0.0254f
    fun metersToPixelsY() = metrics().ydpi / 0.0254f


    const val ballSize = 150
    const val holeSize = ballSize
    const val holeRadius = holeSize / 2
    const val ballRadius = ballSize / 2

    fun maxBallPosition() =
            Point2Df(
                    displayWidth() - ballSize.toFloat(),
                    displayHeight() - ballSize.toFloat()
            )

    val minHolePosition = Point2Df(0f, 0f)

    fun maxHolePosition() = Point2Df(
            displayWidth() - holeSize.toFloat(),
            displayHeight() - holeSize.toFloat()
    )


}