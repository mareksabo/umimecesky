package cz.muni.fi.umimecesky.flappygame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory


/**
 * @author Marek Sabo
 */
object BitmapHelper {

    fun generateImage(resources: Resources, id: Int, width: Int, height: Int): Bitmap =
            Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(resources, id),
                    width,
                    height,
                    true
            )
}