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

package cz.muni.fi.umimecesky.game.shared.model

import java.io.Serializable

data class FillWord constructor(val id: Long,
                                val wordMissing: String,
                                val wordFilled: String,
                                val variant1: String,
                                val variant2: String,
                                val correctVariant: Int,
                                val explanation: String,
                                val grade: Int) : Serializable {

    companion object {
        val EMPTY_WORD = FillWord(1, "", "", "", "", 0, "", 1)
    }


    fun variants(): Pair<String, String> = if (correctVariant == 0)
        replaceWhitespace(variant1) to replaceWhitespace(variant2)
    else
        replaceWhitespace(variant2) to replaceWhitespace(variant1)

    private fun replaceWhitespace(string: String): String {
        return if (string.isBlank()) "␣" else string
    }

}
