package com.shengbojia.notes.adapter

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.shengbojia.notes.utility.DateRegexUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private enum class DateStatus {
    SAME_WEEK,
    SAME_YEAR,
    NOTHING
}

/**
 * Binds the displayed priority for each [CardView] in [NotesListFragment].
 */
@BindingAdapter("app:priority")
fun bindPriority(view: TextView, priority: Int?) {
    if (priority != null) {
        view.text = priority.toString()
    }
}

/**
 * Sets the number picker when inflating layout of [EditNoteFragment].
 */
@BindingAdapter("app:currentPriority")
fun bindCurrentPriority(view: NumberPicker, priority: Int?) {
    if (priority != null) {
        view.minValue = 1
        view.maxValue = 5
        view.value = priority
    }
}

@BindingAdapter("app:writtenDate")
fun bindWrittenDate(view: TextView, dateWritten: Calendar?) {
    val date = dateWritten?.time ?: Date(0)

    val currentTime = Calendar.getInstance()

    val dateFormat = when (dateRelativeToCalendar(date, currentTime)) {
        DateStatus.NOTHING -> DateFormat.getDateInstance(DateFormat.LONG)
        DateStatus.SAME_YEAR -> DateRegexUtil.getLongDateInstanceWithoutYears()
        DateStatus.SAME_WEEK -> SimpleDateFormat("EEEE", Locale.getDefault())
    }

    val dateText = "${dateFormat.format(date)} - ${DateFormat.getTimeInstance(DateFormat.SHORT).format(date)}"

    view.text = dateText

}

private fun dateRelativeToCalendar(date: Date, calendar: Calendar): DateStatus {
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val week = calendar.get(Calendar.WEEK_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    val tempCalendar = Calendar.getInstance()
    tempCalendar.time = date

    val tempDay = tempCalendar.get(Calendar.DAY_OF_WEEK)
    val tempWeek = tempCalendar.get(Calendar.WEEK_OF_MONTH)
    val tempMonth = tempCalendar.get(Calendar.MONTH)
    val tempYear = tempCalendar.get(Calendar.YEAR)

    if (year != tempYear) {
        return DateStatus.NOTHING
    } else if (week != tempWeek) {
        return DateStatus.SAME_YEAR
    } else {
        return DateStatus.SAME_WEEK
    }
}
