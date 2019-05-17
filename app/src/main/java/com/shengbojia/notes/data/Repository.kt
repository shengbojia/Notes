package com.shengbojia.notes.data

interface Repository {

    suspend fun insert(note: Note): Result<Boolean>

    suspend fun update(note: Note): Result<Int>

    suspend fun delete(noteId: Int): Result<Int>

    suspend fun deleteAllNotes(): Result<Int>

    suspend fun getNote(noteId: Int): Result<Note>

    suspend fun getAllNotes(): Result<List<Note>>
}