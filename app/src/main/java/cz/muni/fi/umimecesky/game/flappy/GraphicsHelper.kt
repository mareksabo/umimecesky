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

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint


/**
 * @author Marek Sabo
 */
object GraphicsHelper {

    fun generateImage(resources: Resources, id: Int, width: Int, height: Int): Bitmap =
            Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(resources, id),
                    width,
                    height,
                    true
            )

    fun createThickWordPaint(): Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        paint.textSize = 80f
        paint.textAlign = Paint.Align.CENTER
        return paint
    }

    fun createAnswersPaint(): Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 80f
        paint.textAlign = Paint.Align.CENTER
        return paint
    }
}