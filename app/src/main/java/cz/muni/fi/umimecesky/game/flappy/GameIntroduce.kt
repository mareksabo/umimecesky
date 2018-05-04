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

import android.app.Activity
import cz.muni.fi.umimecesky.game.flappy.sprite.BeeSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.FillWordSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.PipeSprite
import cz.muni.fi.umimecesky.game.flappy.sprite.Sprite
import me.toptas.fancyshowcase.FancyShowCaseQueue


/**
 * @author Marek Sabo
 */
class GameIntroduce(private val activity: Activity, sprites: List<Sprite>, onComplete: () -> Unit) {

    init {
        val queue = FancyShowCaseQueue()
                .add(sprites.first { it is BeeSprite }.run { this.intro(activity) })
                .add(sprites.first { it is FillWordSprite }.run { this.intro(activity) })
                .add(sprites.first { it is PipeSprite }.run { this.intro(activity) })
        queue.setCompleteListener { onComplete() }
        queue.show()
    }

}