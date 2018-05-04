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

import cz.muni.fi.umimecesky.game.shared.model.FillWord

/**
 * @author Marek Sabo
 */
class SortedAnswers {

    constructor(fillWord: FillWord) :
            this(fillWord.variant1, fillWord.variant2, fillWord.correctVariant)

    private constructor(_first: String, _second: String, correct: Int) {

        if (_first > _second) {
            first = _second
            second = _first
            isFirstCorrect = correct == 1
        } else {
            first = _first
            second = _second
            isFirstCorrect = correct == 0
        }
    }

    val isFirstCorrect: Boolean

    val first: String
    val second: String

}