package com.shengbojia.notes.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val title: String,
    val description: String,
    val priority: Int,
    @ColumnInfo(name = "date_written") val dateWritten: Calendar? = Calendar.getInstance()
)

// TODO: Change priority to date or something like that