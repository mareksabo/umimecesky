package cz.muni.fi.umimecesky.pojo

import java.io.Serializable

data class FillWord constructor(val id: Long,
                                val wordMissing: String,
                                val wordFilled: String,
                                val variant1: String,
                                val variant2: String,
                                val correctVariant: Int,
                                val explanation: String,
                                val grade: Int) : Serializable {

    fun variants(): Pair<String, String> = if (correctVariant == 0)
        replaceWhitespace(variant1) to replaceWhitespace(variant2)
    else
        replaceWhitespace(variant2) to replaceWhitespace(variant1)

    private fun replaceWhitespace(string: String): String {
        return if (string.isBlank()) "␣" else string
    }

}
