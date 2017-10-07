package cz.muni.fi.umimecesky.utils

import org.junit.Test

import java.util.ArrayList
import java.util.Arrays

import cz.muni.fi.umimecesky.pojo.Category

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat


class ConversionTest {

    @Test
    @Throws(Exception::class)
    fun stringNumberToBoolean_zeroToFalse() {
        assertThat(Conversion.stringNumberToBoolean("0"), `is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun stringNumberToBoolean_oneToTrue() {
        assertThat(Conversion.stringNumberToBoolean("1"), `is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun convertCategoriesToIDs() {
        val categories = ArrayList<Category>()
        categories.add(Category(1, "First Category"))
        categories.add(Category(2, "Second Category"))
        assertThat(Conversion.convertCategoriesToIDs(categories), `is`(Arrays.asList(1, 2)))
    }

}