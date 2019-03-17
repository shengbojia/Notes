package com.shengbojia.notes.data

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for handling data operations.
 */
class NoteRepository private constructor(
    private val noteDao: NoteDao
) {

    @WorkerThread
    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    @WorkerThread
    suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    @WorkerThread
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