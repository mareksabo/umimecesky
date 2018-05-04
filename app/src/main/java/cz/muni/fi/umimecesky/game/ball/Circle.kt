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

package cz.muni.fi.umimecesky.game.ball

import cz.muni.fi.umimecesky.game.ball.Dimensions.ballRadius

/**
 * @author Marek Sabo
 */
open class Circle(val middle: Point2Df, private val radius: Int) {

    fun isRelativelyClose(another: Circle): Boolean {
        val distanceBetweenHoles = radius + another.radius + ballRadius
        return isInRadius(another, distanceBetweenHoles)
    }

    fun isTouching(another: Circle): Boolean {
        val distanceBetweenHoles = radius + another.radius
        return isInRadius(another, distanceBetweenHoles)
    }

    private fun isInRadius(another: Circle, distanceBetweenHoles: Int) =
            Math.abs(middle.x - another.middle.x) <= distanceBetweenHoles
                    && Math.abs(middle.y - another.middle.y) <= distanceBetweenHoles
}