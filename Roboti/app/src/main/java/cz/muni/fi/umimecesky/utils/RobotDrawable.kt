package cz.muni.fi.umimecesky.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import cz.muni.fi.umimecesky.R
import java.security.SecureRandom
import java.util.*

class RobotDrawable(private val activity: Activity) {

    private val robotNames = ArrayList<String>()

    private val random = SecureRandom()

    init {
        val drawables = R.drawable::class.java.fields
        for (f in drawables) {
            val fieldName = f.name
            if (fieldName.startsWith("robot")) {
                robotNames.add(f.name)
            }
        }
    }

    fun removeRobotDrawable(): Drawable {

        val resources = activity.resources
        val randomInt = random.nextInt(robotNames.size)
        val randomDrawableName = robotNames.removeAt(randomInt)
        val resourceId = resources.getIdentifier(randomDrawableName, "drawable",
                activity.packageName)
        return resources.getDrawable(resourceId)
    }
}
