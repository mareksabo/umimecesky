package cz.muni.fi.umimecesky.labyrinth

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import cz.muni.fi.umimecesky.db.helper.wordOpenHelper
import cz.muni.fi.umimecesky.labyrinth.Dimensions.ballRadius
import cz.muni.fi.umimecesky.labyrinth.Dimensions.ballSize
import cz.muni.fi.umimecesky.labyrinth.Dimensions.defaultTextSize
import cz.muni.fi.umimecesky.labyrinth.Dimensions.holeRadius
import cz.muni.fi.umimecesky.labyrinth.Dimensions.holeSize
import cz.muni.fi.umimecesky.labyrinth.Dimensions.maxHolePosition
import cz.muni.fi.umimecesky.labyrinth.Dimensions.minHolePosition
import cz.muni.fi.umimecesky.labyrinth.hole.Hole
import cz.muni.fi.umimecesky.labyrinth.hole.HoleCircle
import cz.muni.fi.umimecesky.labyrinth.hole.HoleView
import cz.muni.fi.umimecesky.labyrinth.hole.ResultHole
import cz.muni.fi.umimecesky.pojo.FillWord
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.random
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.padding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.sp
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Marek Sabo
 */
class SimulationView(context: Context) : FrameLayout(context) {

    companion object {
        private val interpolator = DecelerateInterpolator(1.5f)
        val EMPTY_WORD = FillWord(1, "", "", "", "", 0, "", 1)
    }

    private val sensorListener = BallSensorListener(context)

    private val ball by lazy { Ball(context) }
    private lateinit var correctHole: ResultHole
    private lateinit var incorrectHole: ResultHole
    private lateinit var holes: List<Hole>
    private lateinit var correctHoleView: HoleView
    private lateinit var incorrectHoleView: HoleView

    private var canRoll = AtomicBoolean(false)
    private var canPressScreen = AtomicBoolean(false)

    private val textView = TextView(context)

    private var currentWord: FillWord = prefs.lastRandomWord

    private fun createHoles(amount: Int): List<Hole> {
        val circles = ArrayList<Circle>(amount + 3)
        circles.add(Circle(ball.initialPosition, ballRadius))
        circles.add(correctHole.circle)
        circles.add(incorrectHole.circle)
        val holes = ArrayList<Hole>(amount)
        val maxHolePosition = maxHolePosition()

        loop@ for (i in 0 until amount) {
            var randomPoint: Point2Df
            var holeCircle: HoleCircle
            var j = 0
            do {
                randomPoint = Point2Df(random.nextFloat() * maxHolePosition.x,
                        random.nextFloat() * maxHolePosition.y)
                holeCircle = HoleCircle(randomPoint)
                j++
                if (j > 1000) {
                    context.toast("Nemůžu vytvořit tolik děr!")
                    break@loop
                }
            } while (!validateHolePosition(circles, holeCircle))
            circles.add(holeCircle)
            holes.add(Hole(holeCircle))
        }
        return holes
    }

    private fun validateHolePosition(circles: List<Circle>, holeCircle: Circle) =
            circles.none { it.isRelativelyClose(holeCircle) }

    fun startSimulation() = sensorListener.startSimulation()
    fun stopSimulation() = sensorListener.stopSimulation()

    init {
        setupView()
        layoutTransition = LayoutTransition()
        textView.textColor = Color.WHITE
        textView.textSize = (defaultTextSize + sp(5))
    }

    private fun setupView() {
        setResultHoles()
        correctHoleView = setupHoleView(correctHole)
        incorrectHoleView = setupHoleView(incorrectHole)

        textView.text = currentWord.wordMissing
        textView.gravity = Gravity.CENTER
        textView.padding = 10
        addView(textView)
        canPressScreen.set(true)
    }

    private fun setupHolesAndBall() {
        holes = createHoles(prefs.holesAmount)
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.w("onTouchEvent, can press", "${canPressScreen.get()}")
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

    private fun setResultHoles() {
        if (currentWord == EMPTY_WORD) {
            currentWord = context.wordOpenHelper.getRandomWordWithSmallVariants(prefs.holeWordGrade)
            prefs.lastRandomWord = currentWord
        }
        val (variant1, variant2) = currentWord.variants()
        val pos = if (random.nextBoolean()) minHolePosition else maxHolePosition()
        val pos2 = if (pos == minHolePosition) maxHolePosition() else minHolePosition
        if (currentWord.correctVariant == 0) {
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
        addView(holeView.textViewInside, ViewGroup.LayoutParams(holeSize, holeSize))
        return holeView
    }

    override fun onDraw(canvas: Canvas) {
        if (!canRoll.get()) return

        when {
            ball.checkTouching(incorrectHole) -> {
                ball.reverseVelocity()
                incorrectHoleView.textViewInside.setTextColor(Color.parseColor("#FF4500")) // #FFA500
            }
            ball.checkInside(correctHole) -> {
                correctHoleAction()
            }
            else -> {
                holes.singleOrNull { ball.checkInside(it) }
                        ?.let { otherHolesAction(it) }
            }
        }

        ball.computeMove(sensorListener.sensor.x, sensorListener.sensor.y)

        invalidate()
    }

    private fun correctHoleAction() {
        correctHoleView.textViewInside.setTextColor(Color.parseColor("#30d330"))
        canRoll.set(false)
        val animator = createAnimation(correctHole)
        animator.withEndAction {
            removeAllViews()
            currentWord = EMPTY_WORD
            setupView()
            ball.recreateBall({})
        }
        animator.start()
    }

    private fun otherHolesAction(holeWithBall: Hole) {
        canRoll.set(false)
        val animator = createAnimation(holeWithBall)
        animator.withEndAction {
            ball.recreateBall {
                canRoll.set(true)
                invalidate()
            }
        }
        animator.start()
    }

    private fun createAnimation(hole: Hole): ViewPropertyAnimator {
        val animator = ball.animate()!!
        animator.translationX(hole.middle().x + holeRadius - ballRadius)
                .translationY(hole.middle().y + holeRadius - ballRadius)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0f)
        animator.duration = 800L
        animator.interpolator = interpolator
        return animator
    }

}
