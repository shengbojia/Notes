package com.shengbojia.notes.data

interface Repository {

    suspend fun insert(note: Note): Result<Boolean>

    suspend fun update(note: Note): Result<Int>

    suspend fun delete(noteId: String): Result<Int>

    suspend fun deleteAllNotes(): Result<Int>

    suspend fun getNote(noteId: String): Result<Note>

    suspend fun getAllNotes(): Result<List<Note>>
}