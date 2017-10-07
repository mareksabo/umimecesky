package cz.muni.fi.umimecesky.pojo

class FillWord internal constructor(val id: Long, val wordMissing: String, val wordFilled: String, val variant1: String, val variant2: String,
                                    val correctVariant: Int, val explanation: String, val grade: Int, val isVisible: Boolean) {

    override fun toString(): String {
        return "FillWord{" +
                "id=" + id +
                ", wordMissing='" + wordMissing + '\'' +
                ", wordFilled='" + wordFilled + '\'' +
                ", variant1='" + variant1 + '\'' +
                ", variant2='" + variant2 + '\'' +
                ", correctVariant=" + correctVariant +
                ", explanation='" + explanation + '\'' +
                ", grade=" + grade +
                ", visibility=" + isVisible +
                '}'
    }
}
