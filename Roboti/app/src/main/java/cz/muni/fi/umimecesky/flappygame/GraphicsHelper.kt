package cz.muni.fi.umimecesky.flappygame

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

    fun createFillWordPaint(): Paint {
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