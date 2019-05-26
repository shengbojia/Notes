package com.shengbojia.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

/**
 * Model class for a Note.
 *
 * @param id the id of the note
 * @param title the title of the note
 * @param description the main text of the note
 * @param dateWritten the time that the note was last saved
 */
@Entity(
    tableName = "note_table",
    indices = [Index("id")]
)
data class Note(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    @ColumnInfo(name = "date_written") val dateWritten: Calendar? = Calendar.getInstance()
)
