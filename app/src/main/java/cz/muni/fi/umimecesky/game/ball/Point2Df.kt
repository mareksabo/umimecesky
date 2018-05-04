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

/**
 * @author Marek Sabo
 */
class Point2Df(var x : Float, var y : Float) {
    constructor(point2Df: Point2Df) : this(point2Df.x, point2Df.y)

    override fun toString(): String = "[$x, $y]"

    fun setTo(point2Df: Point2Df) {
        this.x = point2Df.x
        this.y = point2Df.y
    }

    fun distance(point2Df: Point2Df): Float {
        return Math.hypot((x - point2Df.x).toDouble(), (y - point2Df.y).toDouble()).toFloat()
    }

    fun distance(otherX: Float, otherY: Float): Float {
        return Math.hypot((x - otherX).toDouble(), (y - otherY).toDouble()).toFloat()
    }
}