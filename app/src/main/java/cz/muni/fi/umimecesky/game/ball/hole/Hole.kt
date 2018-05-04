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

package cz.muni.fi.umimecesky.game.ball.hole

import cz.muni.fi.umimecesky.game.ball.Circle
import cz.muni.fi.umimecesky.game.ball.Dimensions.holeRadius
import cz.muni.fi.umimecesky.game.ball.Point2Df

/**
 * @author Marek Sabo
 */
open class Hole(middlePoint: Point2Df) : Circle(middlePoint, holeRadius) {

    override fun toString(): String = "Hole [$middle.x, $middle.y]"

}