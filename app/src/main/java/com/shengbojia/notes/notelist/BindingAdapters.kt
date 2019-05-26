package com.shengbojia.notes.notelist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.shengbojia.notes.utility.DateRegexUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Enum class for the different possible relations between two [Date]s.
 */
private enum class DateStatus {
    SAME_WEEK,
    SAME_YEAR,
    NOTHING
}

/**
 * Sets the date string to display in each note. Changes depending on when the note was written,
 * e.g. Weekday if written this week, Month + Day if outside of this week but same year, etc.
 */
@BindingAdapter("app:writtenDate")
fun bindWrittenDate(view: TextView, dateWritten: Calendar?) {
    val date = dateWritten?.time ?: Date(0)

    val currentTime = Calendar.getInstance()

    val dateFormat = when (dateRelativeToCalendar(date, currentTime)) {
        DateStatus.NOTHING -> DateFormat.getDateInstance(DateFormat.LONG)
        DateStatus.SAME_YEAR -> DateRegexUtil.removeYears(DateFormat.getDateInstance(DateFormat.LONG))
        DateStatus.SAME_WEEK -> SimpleDateFormat("EEEE", Locale.getDefault())
    }

    val dateText = "${dateFormat.format(date)} - ${DateFormat.getTimeInstance(DateFormat.SHORT).format(date)}"

    view.text = dateText

}

/**
 * Determines whether a [Date] and a [Calendar] are within the same year, week, or neither.
 */
private fun dateRelativeToCalendar(date: Date, calendar: Calendar): DateStatus {
    val week = calendar.get(Calendar.WEEK_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)

    val tempCalendar = Calendar.getInstance()
    tempCalendar.time = date

    val tempWeek = tempCalendar.get(Calendar.WEEK_OF_YEAR)
    val tempYear = tempCalendar.get(Calendar.YEAR)

    if (year != tempYear) {
        return DateStatus.NOTHING
    } else if (week != tempWeek) {
        return DateStatus.SAME_YEAR
    } else {
        return DateStatus.SAME_WEEK
    }
}
