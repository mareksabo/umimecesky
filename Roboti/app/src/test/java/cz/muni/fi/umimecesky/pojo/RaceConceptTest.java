package cz.muni.fi.umimecesky.pojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RaceConceptTest {


    @Test
    public void levelProgress() throws Exception {
        final int MAX_LEVELS = 4;
        RaceConcept raceConcept = new RaceConcept("Name", Arrays.asList(1, 2), MAX_LEVELS);
        float expectedResult = 1.0f / MAX_LEVELS;
        assertThat(raceConcept.levelProgress(), is(expectedResult));
    }

}