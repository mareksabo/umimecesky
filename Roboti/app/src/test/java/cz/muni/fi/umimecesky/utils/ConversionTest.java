package cz.muni.fi.umimecesky.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.muni.fi.umimecesky.pojo.Category;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ConversionTest {

    @Test
    public void stringNumberToBoolean_zeroToFalse() throws Exception {
        assertThat(Conversion.stringNumberToBoolean("0"), is(false));
    }

    @Test
    public void stringNumberToBoolean_oneToTrue() throws Exception {
        assertThat(Conversion.stringNumberToBoolean("1"), is(true));
    }

    @Test
    public void convertCategoriesToIDs() throws Exception {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "First Category"));
        categories.add(new Category(2, "Second Category"));
        assertThat(Conversion.convertCategoriesToIDs(categories), is(Arrays.asList(1,2)) );
    }

}