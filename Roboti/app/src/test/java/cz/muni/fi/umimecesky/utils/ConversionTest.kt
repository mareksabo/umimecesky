package cz.muni.fi.umimecesky.utils

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test


class ConversionTest {

    @Test
    @Throws(Exception::class)
    fun stringNumberToBooleanZeroToFalse() {
        assertThat(Conversion.stringNumberToBoolean("0"), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun stringNumberToBooleanOneToTrue() {
        assertThat(Conversion.stringNumberToBoolean("1"), `is`(true))
    }
}