package cz.muni.fi.umimecesky.labyrinth

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.Surface
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import cz.muni.fi.umimecesky.db.helper.wordOpenHelper
import cz.muni.fi.umimecesky.labyrinth.Constant.ballRadius
import cz.muni.fi.umimecesky.labyrinth.Constant.ballSize
import cz.muni.fi.umimecesky.labyrinth.Constant.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Constant.holeSize
import cz.muni.fi.umimecesky.labyrinth.Constant.maxHolePosition
import cz.muni.fi.umimecesky.labyrinth.Constant.minHolePosition
import cz.muni.fi.umimecesky.labyrinth.hole.Hole
import cz.muni.fi.umimecesky.labyrinth.hole.HoleCircle
import cz.muni.fi.umimecesky.labyrinth.hole.HoleView
import cz.muni.fi.umimecesky.labyrinth.hole.ResultHole
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.random
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.longToast
import org.jetbrains.anko.padding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColor
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

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

    private val ball = Ball(context)
    private lateinit var correctHole: ResultHole
    private lateinit var incorrectHole: ResultHole
    private lateinit var holes: List<Hole>
    private lateinit var incorrectHoleView: HoleView

    private var canRoll = AtomicBoolean(false)
    private var canPressScreen = AtomicBoolean(false)

    private val textView = TextView(context)

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
        setupView()
        layoutTransition = LayoutTransition()
        textView.textColor = Color.WHITE
        textView.textSize += 4f
    }

    private fun setupView() {
        setNewRandomWord()
        setupHoleView(correctHole)
        incorrectHoleView = setupHoleView(incorrectHole)

        textView.text = randomWord.wordMissing
        textView.gravity = Gravity.CENTER
        textView.padding = 10
        addView(textView)
        canPressScreen.set(true)
    }

    private fun setupHolesAndBall() {
        holes = createHoles(10)
        for (hole in holes) setupHoleView(hole)
        textView.parent?.let {
            (textView.parent as ViewGroup).removeView(textView)
            textView.gravity = Gravity.START or Gravity.BOTTOM
            textView.rightPadding += holeSize
            addView(textView)
        }

        ball.parent?.let { (ball.parent as ViewGroup).removeView(ball) }
        addView(ball, ViewGroup.LayoutParams(ballSize, ballSize))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN && canPressScreen.get()) {
            canPressScreen.set(false)

            setupHolesAndBall()

            Observable.just(true)
                    .delay(500L, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        canRoll.set(true)
                        invalidate()
                    }
        }
        return super.onTouchEvent(event)
    }

    private var randomWord: FillWord = context.wordOpenHelper.getRandomWord()

    private fun setNewRandomWord() {
        do {
            randomWord = context.wordOpenHelper.getRandomWord()
        } while (randomWord.variant1.length >= 3 || randomWord.variant2.length >= 3)
        val (variant1, variant2) = randomWord.variants()
        // TODO: better fix long answers
        val pos = if (random.nextBoolean()) minHolePosition else maxHolePosition
        val pos2 = if (pos == minHolePosition) maxHolePosition else minHolePosition
        if (randomWord.correctVariant == 0) {
            correctHole = ResultHole(HoleCircle(pos), variant1, true)
            incorrectHole = ResultHole(HoleCircle(pos2), variant2, false)
        } else {
            correctHole = ResultHole(HoleCircle(pos), variant2, true)
            incorrectHole = ResultHole(HoleCircle(pos2), variant1, false)
        }
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
        if (!canRoll.get()) return

        when {
            ball.checkTouching(incorrectHole) -> {
                ball.reverseVelocity()
                incorrectHoleView.textInside.setTextColor(Color.parseColor("#FFA500")) // #FF4500
            }
            ball.checkInside(correctHole) -> runFallingBallAnimation(correctHole, {
                removeAllViews()
                setupView()
            })
            else -> holes
                    .singleOrNull { ball.checkInside(it) }
                    .let {
                        it?.let { it1 ->
                            runFallingBallAnimation(it1, {
                                canRoll.set(true)
                            })
                        }
                    }
        }

        ball.computeMove(sensor.x, sensor.y)

        invalidate()
    }

    private fun runFallingBallAnimation(hole: Hole, f: () -> Unit) {
        canRoll.set(false)
        val animator = ball.animate()
        animator.translationX(hole.middle().x + holeRadius - ballRadius)
                .translationY(hole.middle().y + holeRadius - ballRadius)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0f)
                .withEndAction {
                    ball.recreateBall()
                    f()
                }
        animator.duration = 800L
        animator.interpolator = interpolator
        animator.start()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

}
