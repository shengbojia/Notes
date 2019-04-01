package com.shengbojia.notes.adapter

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.util.*

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

    if (dateWritten != null) {
        val date = DateFormat.getDateInstance(DateFormat.LONG).format(dateWritten.time)
        view.text = date
    }
}