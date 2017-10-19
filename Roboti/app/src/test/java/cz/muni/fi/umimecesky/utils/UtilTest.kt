package cz.muni.fi.umimecesky.utils

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class UtilTest {

    @Test
    @Throws(Exception::class)
    fun probabilityTrueIsAlwaysTrue() {
        assertThat(Util.probabilityTrue(1.0), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun probabilityTrueIsAlwaysFalse() {
        assertThat(Util.probabilityTrue(0.0), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2placesShouldRoundUp() {
        assertThat(Util.roundBy2places(12.3456), `is`(12.35))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2placesShouldRoundDown() {
        assertThat(Util.roundBy2places(12.3446), `is`(12.34))
    }
}