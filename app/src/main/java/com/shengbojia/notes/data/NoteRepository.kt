package com.shengbojia.notes.data

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for handling data operations.
 */
class NoteRepository private constructor(
    private val noteDao: NoteDao
) {

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    suspend fun deleteAllNotes() {
        withContext(Dispatchers.IO) {
            noteDao.deleteAllNotes()
        }
    }

    fun getNote(noteId: Int): LiveData<Note> = noteDao.getNote(noteId)

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    // Singleton instantiation
    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(noteDao: NoteDao): NoteRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteRepository(noteDao).also { INSTANCE = it }
            }
    }

}