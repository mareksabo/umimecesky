package cz.muni.fi.umimecesky.utils

import org.junit.Test

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

class UtilTest {

    @Test
    @Throws(Exception::class)
    fun probabilityTrue_AlwaysTrue() {
        assertThat(Util.probabilityTrue(1.0), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun probabilityTrue_AlwaysFalse() {
        assertThat(Util.probabilityTrue(0.0), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2places_roundUp() {
        assertThat(Util.roundBy2places(12.3456), `is`(12.35))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2places_roundDown() {
        assertThat(Util.roundBy2places(12.3446), `is`(12.34))
    }
}