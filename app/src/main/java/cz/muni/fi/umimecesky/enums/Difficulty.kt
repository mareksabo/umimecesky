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

package cz.muni.fi.umimecesky.enums

/**
 * Represents word difficulty (taken from database - grade).
 * Name must be unique.
 *
 * @author Marek Sabo
 */
sealed class Difficulty(val name: String, val grade: Int) {
    object Easy : Difficulty("Lehké", 1)
    object Medium : Difficulty("Težší", 2)
    object Hard : Difficulty("Náročné", 3)
    object Unknown : Difficulty("", -1)

    companion object {
        val difficulties by lazy { arrayOf(Easy, Medium, Hard) }
        val difficultyNames by lazy { difficulties.map { it.name } }
    }

    override fun toString() = name
}

fun String.toDifficulty(): Difficulty {
    val index = Difficulty.difficultyNames.indexOf(this)
    return if (index >= 0) Difficulty.difficulties[index] else Difficulty.Unknown
}

