package cz.muni.fi.umimecesky.game.robots

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import cz.muni.fi.umimecesky.R

object RobotDrawable {

    private val robotNames : List<String> =
            R.drawable::class.java.fields
                    .map { it.name }
                    .filter { it.startsWith("robot") }

    private val uniqueRandom: UniqueRandom = UniqueRandom(robotNames.size)

    fun getRobotDrawable(context: Context): Drawable {

        val randomInt = uniqueRandom.next()
        val randomDrawableName = robotNames[randomInt]
        val resourceId = context.resources.getIdentifier(randomDrawableName, "drawable",
                context.packageName)
        return ContextCompat.getDrawable(context, resourceId)!!
    }
}
