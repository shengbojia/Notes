package com.shengbojia.notes.utility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.DateFormat
import java.util.*

/**
 * Local unit test for some of the regex utility functions.
 */
class DateRegexUtilTest {

    @Test
    fun removeYears_CanadaLong_NoYears() {
        val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA)
        assertThat(DateRegexUtil.removeYears(df).toString())
            .doesNotContain(CURRENT_YEAR)
    }

    @Test
    fun removeYears_ChinaLong_NoYears() {
        val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINESE)
        assertThat(DateRegexUtil.removeYears(df).toString())
            .doesNotContain(CURRENT_YEAR + "年")
    }

    companion object {
        private const val CURRENT_YEAR = "2019"

    }
}