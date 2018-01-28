package cz.muni.fi.umimecesky.utils

import cz.muni.fi.umimecesky.logic.RobotLogic.Companion.probabilityTrue
import cz.muni.fi.umimecesky.logic.RobotLogic.Companion.roundBy2places
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class RobotUtilTest {

    @Test
    @Throws(Exception::class)
    fun probabilityTrueIsAlwaysTrue() {
        assertThat(probabilityTrue(1.0), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun probabilityTrueIsAlwaysFalse() {
        assertThat(probabilityTrue(0.0), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2placesShouldRoundUp() {
        assertThat(roundBy2places(12.3456), `is`(12.35))
    }

    @Test
    @Throws(Exception::class)
    fun roundBy2placesShouldRoundDown() {
        assertThat(roundBy2places(12.3446), `is`(12.34))
    }
}