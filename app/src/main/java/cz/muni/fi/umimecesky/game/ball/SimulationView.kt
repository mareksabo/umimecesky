/*
 * Copyright (c) 2018 Marek Sabo
 * Modifications copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.ball

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
import cz.muni.fi.umimecesky.game.ball.Dimensions.ballRadius
import cz.muni.fi.umimecesky.game.ball.Dimensions.ballSize
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeRadius
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeSize
import cz.muni.fi.umimecesky.game.ball.Dimensions.maxHolePosition
import cz.muni.fi.umimecesky.game.ball.Dimensions.minHolePosition
import cz.muni.fi.umimecesky.game.ball.hole.Hole
import cz.muni.fi.umimecesky.game.ball.hole.HoleView
import cz.muni.fi.umimecesky.game.ball.hole.ResultHole
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.model.FillWord.Companion.EMPTY_WORD
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.random
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.padding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.sp
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Marek Sabo
 */
class SimulationView(context: Context) : FrameLayout(context) {

    companion object {
        private val interpolator = DecelerateInterpolator(1.5f)
    }

    private val sensorListener = BallSensorListener(context)
    private val ball by lazy { Ball(context) }

    private lateinit var correctHole: ResultHole
    private lateinit var incorrectHole: ResultHole
    private lateinit var holes: List<Hole>
    private lateinit var correctHoleView: HoleView
    private lateinit var incorrectHoleView: HoleView
    private val textView = TextView(context)

    private var canRoll = AtomicBoolean(false)
    private var canPressScreen = AtomicBoolean(false)

    private var currentWord: FillWord = prefs.lastRandomWord

    fun startSimulation() = sensorListener.startSimulation()
    fun stopSimulation() = sensorListener.stopSimulation()

    init {
        setupView()
        layoutTransition = LayoutTransition()
    }

    private fun setupView() {
        setResultHoles()
        correctHoleView = setupHoleView(correctHole)
        incorrectHoleView = setupHoleView(incorrectHole)

        setupMainText()
        canPressScreen.set(true)
    }

    private fun setResultHoles() {
        if (currentWord == EMPTY_WORD) {
            currentWord = context.wordOpenHelper.getRandomWordWithSmallVariants(prefs.holeWordGrade)
            prefs.lastRandomWord = currentWord
        }
        val (variant1, variant2) = currentWord.variants()
        val (pos, pos2) =
                if (random.nextBoolean()) minHolePosition to maxHolePosition()
                else (maxHolePosition() to minHolePosition)
        correctHole = ResultHole(pos, variant1)
        incorrectHole = ResultHole(pos2, variant2)
    }

    private fun setupHoleView(hole: Hole): HoleView {
        val holeView = HoleView(hole, context)
        addView(holeView, ViewGroup.LayoutParams(holeSize, holeSize))
        addView(holeView.textViewInside, ViewGroup.LayoutParams(holeSize, holeSize))
        return holeView
    }

    private fun setupMainText() {
        textView.text = currentWord.wordMissing
        textView.textColor = Color.WHITE
        textView.textSize = (Dimensions.defaultTextSize + sp(5))
        textView.gravity = Gravity.CENTER
        textView.padding = 10
        addView(textView)
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

    private fun setupHolesAndBall() {
        holes = createHoles(prefs.holesAmount)
        if (holes.size < prefs.holesAmount) context.toast("Nemůžu vytvořit tolik děr!")
        for (hole in holes) setupHoleView(hole)
        textView.parent?.apply {
            (textView.parent as ViewGroup).removeView(textView)
            textView.gravity = Gravity.START or Gravity.BOTTOM
            textView.rightPadding += holeSize
            addView(textView)
        }

        ball.parent?.let { (ball.parent as ViewGroup).removeView(ball) }
        addView(ball, ViewGroup.LayoutParams(ballSize, ballSize))
    }

    private fun createHoles(amount: Int): List<Hole> {
        val initialPosition = Hole(ball.initialPosition)
        val temporaryHoles = listOf(initialPosition, correctHole, incorrectHole)
        val holes = temporaryHoles.toMutableList()

        for (i in 1..amount) {
            val hole = generateRandomHole(maxHolePosition(), holes)
            hole?.let { holes.add(it) } ?: break
        }
        holes.removeAll(temporaryHoles)
        return holes
    }

    private fun generateRandomHole(maxHolePosition: Point2Df, holes: MutableList<Hole>): Hole? {
        var hole: Hole

        repeat (1000) {
            hole = Hole(Point2Df(random.nextFloat() * maxHolePosition.x,
                    random.nextFloat() * maxHolePosition.y))
            if (holes.none { it.isRelativelyClose(hole) }) return hole
        }
        return null
    }

    override fun onDraw(canvas: Canvas) {
        if (!canRoll.get()) return
        when {
            ball.checkTouching(incorrectHole) -> incorrectHoleAction()
            ball.checkInside(correctHole) -> correctHoleAction()
            else -> holes.singleOrNull { ball.checkInside(it) }
                    ?.let { otherHoleAction(it) }
        }

        ball.computeMove(sensorListener.sensor.x, sensorListener.sensor.y)

        invalidate()
    }

    private fun incorrectHoleAction() {
        ball.reverseVelocity()
        incorrectHoleView.textViewInside.setTextColor(Color.parseColor("#FF4500")) // #FFA500
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

    private fun otherHoleAction(holeWithBall: Hole) {
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
        animator.translationX(hole.middle.x + holeRadius - ballRadius)
                .translationY(hole.middle.y + holeRadius - ballRadius)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0f)
        animator.duration = 800L
        animator.interpolator = interpolator
        return animator
    }

}
