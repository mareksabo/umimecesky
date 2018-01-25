package cz.muni.fi.umimecesky.utils

object Util {

    fun probabilityTrue(probabilityToReturnTrue: Double): Boolean =
            Math.random() >= 1.0 - probabilityToReturnTrue

    fun roundBy2places(numberToRound: Double): Double =
            Math.round(numberToRound * 100).toDouble() / 100
}
