package cz.muni.fi.umimecesky.labyrinth

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import cz.muni.fi.umimecesky.labyrinth.Constant.ballRadius
import cz.muni.fi.umimecesky.labyrinth.Constant.ballSize
import cz.muni.fi.umimecesky.labyrinth.Constant.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Constant.holeSize
import cz.muni.fi.umimecesky.labyrinth.Constant.maxHolePosition
import cz.muni.fi.umimecesky.labyrinth.Constant.minHolePosition
import cz.muni.fi.umimecesky.labyrinth.hole.CorrectHole
import cz.muni.fi.umimecesky.labyrinth.hole.Hole
import cz.muni.fi.umimecesky.labyrinth.hole.HoleCircle
import cz.muni.fi.umimecesky.labyrinth.hole.HoleView
import java.util.*

/**
 * @author Marek Sabo
 */
class SimulationView(context: Context) : FrameLayout(context), SensorEventListener {

    companion object {
        private val interpolator = DecelerateInterpolator(1.5f)
    }

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val defaultDisplay = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var sensor = Point2Df(0f, 0f)

    private val random = Random()
    private val ball = Ball(context)
    private val correctHole = CorrectHole(HoleCircle(minHolePosition), "i")
    private val incorrectHole = CorrectHole(HoleCircle(maxHolePosition), "y")
    private val holes = createHoles(15)
    private val incorrectHoleView: HoleView

    private fun createHoles(amount: Int): List<Hole> {
        val circles = ArrayList<Circle>(amount + 3)
        circles.add(Circle(Ball.initialPosition, ballRadius))
        circles.add(correctHole.circle)
        circles.add(incorrectHole.circle)
        val holes = ArrayList<Hole>(amount)
        for (i in 0 until amount) {
            var randomPoint: Point2Df
            var holeCircle: HoleCircle
            do {
                randomPoint = Point2Df(random.nextInt(maxHolePosition.x.toInt()).toFloat(),
                        random.nextInt(maxHolePosition.y.toInt()).toFloat())
                holeCircle = HoleCircle(randomPoint)
            } while (!circles.none { it.isTouching(holeCircle) })
            circles.add(holeCircle)
            holes.add(Hole(holeCircle))
        }
        return holes
    }

    /*
             * It is not necessary to get accelerometer events at a very high
             * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
             * automatic low-pass filter, which "extracts" the gravity component
             * of the acceleration. As an added benefit, we use less power and
             * CPU resources.
             */
    fun startSimulation() =
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)


    fun stopSimulation() = sensorManager.unregisterListener(this)

    init {
        for (hole in holes) setupHoleView(hole)
        setupHoleView(correctHole)
        incorrectHoleView = setupHoleView(incorrectHole)
        addView(ball, ViewGroup.LayoutParams(ballSize, ballSize))
    }

    private fun setupHoleView(hole: Hole): HoleView {
        val holeView = HoleView(hole, context)
        addView(holeView, ViewGroup.LayoutParams(holeSize, holeSize))
        addView(holeView.textInside, ViewGroup.LayoutParams(holeSize, holeSize))
        return holeView
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        /*
         * In this application, we need to
         * take into account how the screen is rotated with respect to the
         * sensors (which always return data in a coordinate space aligned
         * to with the screen in its native orientation).
         */

        when (defaultDisplay.rotation) {
            Surface.ROTATION_0 -> {
                sensor.x = event.values[0]
                sensor.y = event.values[1]
            }
            Surface.ROTATION_90 -> {
                sensor.x = -event.values[1]
                sensor.y = event.values[0]
            }
            Surface.ROTATION_180 -> {
                sensor.x = -event.values[0]
                sensor.y = -event.values[1]
            }
            Surface.ROTATION_270 -> {
                sensor.x = event.values[1]
                sensor.y = -event.values[0]
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (ball.isInsideHole) return

        ball.computeMove(sensor.x, sensor.y)

        for (hole in holes) {
            if (ball.checkInside(hole)) {
                ball.isInsideHole = true
                runFallingBallAnimation(hole)
            }
        }

        if (ball.checkInside(incorrectHole)) {
            incorrectHoleView.textInside.setTextColor(Color.RED)
        }

        invalidate()
    }

    private fun runFallingBallAnimation(hole: Hole) {
        val animator = ball.animate()
        animator.translationX(hole.middle().x + holeRadius - ballRadius)
                .translationY(hole.middle().y + holeRadius - ballRadius)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0f)
                .withEndAction { ball.recreateBall() }
        animator.duration = 800L
        animator.interpolator = interpolator
        animator.start()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

}
