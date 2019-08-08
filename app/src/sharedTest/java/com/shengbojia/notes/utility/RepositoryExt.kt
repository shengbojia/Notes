package com.shengbojia.notes.utility

import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import kotlinx.coroutines.runBlocking

/**
 * A run blocking version of Repository.insert() to minimize repetition of runBlocking{ } in tests.
 */
fun Repository.insertNoteBlocking(note: Note) = runBlocking {
    this@insertNoteBlocking.insert(note)
}

fun Repository.getAllNotesBlocking() = runBlocking {
    this@getAllNotesBlocking.getAllNotes()
}