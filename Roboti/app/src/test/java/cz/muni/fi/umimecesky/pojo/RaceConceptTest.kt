package cz.muni.fi.umimecesky.pojo

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RaceConceptTest {


    @Test
    @Throws(Exception::class)
    fun levelProgress() {
        val numberOfLevels = 4
        val raceConcept = RaceConcept("Name", Arrays.asList(1, 2), numberOfLevels)
        val expectedResult = 1.0f / numberOfLevels
        assertThat(raceConcept.levelProgress(), `is`(expectedResult))
    }

}