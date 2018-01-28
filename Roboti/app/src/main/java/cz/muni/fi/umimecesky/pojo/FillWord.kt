package cz.muni.fi.umimecesky.pojo

data class FillWord constructor(val id: Long,
                                val wordMissing: String,
                                val wordFilled: String,
                                val variant1: String,
                                val variant2: String,
                                val correctVariant: Int,
                                val explanation: String,
                                val grade: Int) {

    fun variants(): Pair<String, String> {
        return replaceWhitespace(variant1) to replaceWhitespace(variant2)
    }

    private fun replaceWhitespace(string: String): String {
        return if (string.isBlank()) "␣" else string
    }

}
