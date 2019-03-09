package com.shengbojia.notes.data

import android.arch.persistence.room.TypeConverter
import java.util.Calendar

class Converters {
    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun timestampToCalendar(value: Long): Calendar =
            Calendar.getInstance().apply { timeInMillis = value }
}