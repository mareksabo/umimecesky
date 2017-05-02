package cz.muni.fi.umimecesky.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtilTest {

    @Test
    public void probabilityTrue_AlwaysTrue() throws Exception {
        assertThat(Util.probabilityTrue(1.0), is(true));
    }

    @Test
    public void probabilityTrue_AlwaysFalse() throws Exception {
        assertThat(Util.probabilityTrue(0.0), is(false));
    }

    @Test
    public void roundBy2places_roundUp() throws Exception {
        assertThat(Util.roundBy2places(12.3456), is(12.35));
    }

    @Test
    public void roundBy2places_roundDown() throws Exception {
        assertThat(Util.roundBy2places(12.3446), is(12.34));
    }
}