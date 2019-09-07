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

package cz.muni.fi.umimecesky.game.robots

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import cz.muni.fi.umimecesky.R

object RobotDrawable {

    private val robotNames : List<String> =
            R.drawable::class.java.fields
                    .map { it.name }
                    .filter { it.startsWith("robot") }

    private val uniqueRandom = UniqueRandom(robotNames.size)

    fun getRobotDrawable(context: Context): Drawable {

        val randomInt = uniqueRandom.next()
        val randomDrawableName = robotNames[randomInt]
        val resourceId = context.resources.getIdentifier(randomDrawableName, "drawable",
                context.packageName)
        return ContextCompat.getDrawable(context, resourceId)!!
    }
}
