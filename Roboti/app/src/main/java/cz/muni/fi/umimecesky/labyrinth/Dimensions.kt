package cz.muni.fi.umimecesky.labyrinth

import android.content.res.Resources

/**
 * @author Marek Sabo
 */
// TODO: Make as usual class with singleton. Object will be created Every time settings are changed.
object Dimensions {

    private fun metrics() = Resources.getSystem().displayMetrics
    private fun displayWidth() = metrics().widthPixels
    private fun displayHeight() = metrics().heightPixels

    fun metersToPixelsX() = metrics().xdpi / 0.0254f
    fun metersToPixelsY() = metrics().ydpi / 0.0254f

    fun isTablet(): Boolean {
        return diagonalInches() >= 7.0
    }

    private fun diagonalInches(): Double {
        val xInches = displayWidth() / metrics().xdpi.toDouble()
        val yInches = displayHeight() / metrics().ydpi.toDouble()
        return Math.hypot(xInches, yInches)
    }

    fun deviceDpi(): Int {
        return (Math.hypot(
                displayWidth().toDouble(),
                displayHeight().toDouble())
                / diagonalInches()
                ).toInt()
    }

    private fun dip(value: Int): Int = (value * metrics().density).toInt()
    private fun sp(value: Int): Float = value * metrics().scaledDensity

    val defaultTextSize = if (isTablet()) sp(20) else sp(10)

    val ballSize = if (isTablet()) dip(80) else dip(56)
    val holeSize = ballSize
    val holeRadius = holeSize / 2
    val ballRadius = ballSize / 2

    val minHolePosition = Point2Df(0f, 0f)

    fun maxBallPosition() = maxPosition(ballSize)
    fun maxHolePosition() = maxPosition(holeSize)

    private fun maxPosition(objectSize: Int) = Point2Df(
            displayWidth() - objectSize.toFloat(),
            displayHeight() - objectSize.toFloat()
    )

}