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

package cz.muni.fi.umimecesky.game.flappy;

import android.content.Context
import cz.muni.fi.umimecesky.db.helper.joinCategoryWordOpenHelper
import cz.muni.fi.umimecesky.game.shared.model.FillWord
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import java.util.*

/**
 * @author Marek Sabo
 */
class WordGenerator(private val context: Context, private val raceConcept: RaceConcept) {

    private var words = Stack<FillWord>()

    fun getNextWord(): FillWord {
        if (words.empty()) {
            words = context.joinCategoryWordOpenHelper
                    .getRandomCategoryWords(raceConcept.categoryIDs, prefs.flappyGradeName.grade)
        }
        return words.pop()
    }
}
