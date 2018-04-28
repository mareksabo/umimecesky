package cz.muni.fi.umimecesky.enums

import cz.muni.fi.umimecesky.enums.Gap.Companion.gaps

/**
 * Represents gap between pipes.
 * Size must be unique.
 *
 * @author Marek Sabo
 */
sealed class Gap(val name: String, val size: Int) {

    object Small : Gap("Malá", 276)
    object Medium : Gap("Střední", 300)
    object Big : Gap("Velká", 326)
    object Huge : Gap("Obrovská", 350)
    object Unknown : Gap(Big.name, Big.size)

    companion object {
        val gaps by lazy { arrayOf(Small, Medium, Big, Huge) }
    }

    override fun toString(): String = "$size"
}

fun Int.toGap(): Gap {
    val index = gaps.map { it.size }.indexOf(this)
    return if (index >= 0) gaps[index] else Gap.Unknown
}