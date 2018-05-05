/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.flappy

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import cz.muni.fi.umimecesky.game.flappy.FlappyLogger.Status.CORRECT
import cz.muni.fi.umimecesky.game.flappy.FlappyLogger.Status.OUTSIDE
import cz.muni.fi.umimecesky.game.flappy.FlappyLogger.Status.TOUCH
import cz.muni.fi.umimecesky.game.flappy.FlappyLogger.Status.WRONG
import cz.muni.fi.umimecesky.game.flappy.sprite.BeeSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.CounterSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.FillWordSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.FlowerSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.PipeSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.Sprite
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


/**
 * @author Marek Sabo
 */
@SuppressLint("ViewConstructor")
class GameLogic(private val activity: Activity, private val raceConcept: RaceConcept)
    : SurfaceView(activity), SurfaceHolder.Callback {
    private val thread: Thread

    private lateinit var beeSprite: BeeSprite
    private lateinit var pipe: PipeSprite
    private lateinit var textSprite: FillWordSprite
    private lateinit var counterSprite: CounterSprite
    private lateinit var flowerSprite: FlowerSprite
    private lateinit var sprites: List<Sprite>

    private val wordGenerator = WordGenerator(context, raceConcept)
    private var incorrectAnswer = false
    private val logger = FlappyLogger(activity, prefs.isFlappyGameIntroduced)

    private var currentWord: FillWord = FillWord.EMPTY_WORD
        set(value) {
            field = value
            textSprite.text = value.wordMissing
            Timer().schedule(300) {
                pipe.answers = SortedAnswers(currentWord)
            }
        }

    private val movementSimulator: MovementSimulator

    init {
        holder.addCallback(this)
        movementSimulator = MovementSimulator(holder, this)
        thread = Thread(movementSimulator)
        isFocusable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        beeSprite.doJump()
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        sprites = createSprites()
        currentWord = wordGenerator.getNextWord()
        pipe.answers = SortedAnswers(currentWord)
        movementSimulator.running = true
        if (!prefs.isFlappyGameIntroduced) introduceGame(holder) else startThreadIfNotRunning()
    }

    private fun introduceGame(holder: SurfaceHolder) {
        val canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        GameIntroduce(activity, sprites) {
            prefs.isFlappyGameIntroduced = true
            startThreadIfNotRunning()
        }
    }

    private fun createSprites(): List<Sprite> {
        flowerSprite = FlowerSprite(resources)
        pipe = PipeSprite(resources)
        textSprite = FillWordSprite()
        counterSprite = CounterSprite(raceConcept)
        beeSprite = BeeSprite(resources)
        return listOf(flowerSprite, pipe, textSprite, counterSprite, beeSprite)
//        resetLevel()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        movementSimulator.running = false
        while (true) {
            try {
                thread.join()
                break
            } catch (e: InterruptedException) {
            }
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            super.draw(canvas)
            canvas.drawRGB(140, 195, 255)
            sprites.forEach { it.draw(canvas) }
        }
    }

    fun update() {
        if (beeSprite.isOutsideScreen()) resetLevel(true)
        if (isInCollision(beeSprite, pipe)) resetLevel()
        if (isInCollision(flowerSprite, pipe)) flowerSprite.setFurtherFromPipe()
        if (isInWrongAnswer()) {
            pipe.markIncorrectAnswerRed()
            incorrectAnswer = true
        }

        sprites.forEach { it.move() }

        if (pipe.canChangeWordBehindBee()) {
            if (incorrectAnswer) {
                resetLevel()
            } else {
                counterSprite.increaseCounter()
                log(CORRECT)
                currentWord = wordGenerator.getNextWord()
            }
        }
    }

    // TODO: probably not needed anymore
    private fun startThreadIfNotRunning() {
        if (!thread.isAlive) thread.start()
    }

    private fun resetLevel(isOutsideScreen: Boolean = false) {
        if (incorrectAnswer) log(WRONG) else if (isOutsideScreen) log(OUTSIDE) else log(TOUCH)
        Thread.sleep(500L)
        sprites.forEach { it.reset() }
        incorrectAnswer = false
    }

    private fun isInCollision(beeSprite: BeeSprite, pipeSprite: PipeSprite): Boolean {
        val beeBounds = beeSprite.createRect()

        return beeBounds.intersect(pipeSprite.createTopRect()) ||
                beeBounds.intersect(pipeSprite.createMidRect()) ||
                beeBounds.intersect(pipeSprite.createBotRect())
    }

    private fun isInCollision(flowerSprite: FlowerSprite, pipeSprite: PipeSprite): Boolean {
        return flowerSprite.createRect().intersect(pipeSprite.createBotRect())
    }

    private fun isInWrongAnswer() = beeSprite.createRect().intersect(pipe.createWrongAnswerRect())

    private fun log(status: FlappyLogger.Status) {
        if (activity.hasWindowFocus() && timedBoolean()) {
            logger.logEvent(status, raceConcept, counterSprite.currentCount, textSprite.text)
            timedBoolean(true)
        }
    }

    private var start = 0L

    private fun timedBoolean(startTimer: Boolean = false): Boolean =
            if (startTimer) {
                start = System.nanoTime()
                true
            } else System.nanoTime() - start > TimeUnit.MILLISECONDS.toNanos(100)
}
