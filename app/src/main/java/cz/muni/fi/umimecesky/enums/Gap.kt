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

import cz.muni.fi.umimecesky.enums.Gap.Companion.gaps

/**
 * Represents gap between pipes.
 * Size must be unique.
 *
 * @author Marek Sabo
 */
sealed class Gap(val name: String, val size: Int) {

    object Mini : Gap("Mini", 276)
    object Small : Gap("Malá", 300)
    object Medium : Gap("Střední", 326)
    object Big : Gap("Mega", 350)
    object Huge : Gap("Giga", 400)
    object Unknown : Gap(Medium.name, Medium.size)

    companion object {
        val gaps by lazy { arrayOf(Huge, Big, Medium, Small, Mini) }
    }

    override fun toString(): String = "$size"
}

fun Int.toGap(): Gap {
    val index = gaps.map { it.size }.indexOf(this)
    return if (index >= 0) gaps[index] else Gap.Unknown
}