package cz.muni.fi.umimecesky.pojo

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import java.util.Arrays

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

@RunWith(MockitoJUnitRunner::class)
class RaceConceptTest {


    @Test
    @Throws(Exception::class)
    fun levelProgress() {
        val MAX_LEVELS = 4
        val raceConcept = RaceConcept("Name", Arrays.asList(1, 2), MAX_LEVELS)
        val expectedResult = 1.0f / MAX_LEVELS
        assertThat(raceConcept.levelProgress(), `is`(expectedResult))
    }

}