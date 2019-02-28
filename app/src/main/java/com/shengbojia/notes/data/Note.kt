package com.shengbojia.notes.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var title: String,
    var description: String,
    var priority: Int
)

// TODO: Change priority to date or something like that