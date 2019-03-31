package com.shengbojia.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

/**
 * [Note]
 */
@Entity(
    tableName = "note_table",
    indices = [Index("id")]
)
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val title: String,
    val description: String,
    @ColumnInfo(name = "date_written") val dateWritten: Calendar? = Calendar.getInstance()
)
