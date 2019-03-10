package com.shengbojia.notes.adapter

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:priority")
fun bindPriority(view: TextView, priority: Int?) {
    if (priority != null) {
        view.text = priority.toString()
    }
}

@BindingAdapter("app:currentPriority")
fun bindCurrentPriority(view: NumberPicker, priority: Int?) {
    if (priority != null) {
        view.minValue = 1
        view.maxValue = 5
        view.value = priority
    }
}