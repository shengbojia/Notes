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
){

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

    /*
    // TODO: Use Kotlin's co-routines instead of AsyncTask
    private class InsertNoteAsyncTask internal constructor(
        private val noteDao: NoteDao
    ) : AsyncTask<Note, Unit, Unit>() {

        override fun doInBackground(vararg params: Note) {
            noteDao.insert(params[0])
            return
        }
    }

    private class UpdateNoteAsyncTask internal constructor(
        private val noteDao: NoteDao
    ): AsyncTask<Note, Unit, Unit>() {

        override fun doInBackground(vararg params: Note) {
            noteDao.update(params[0])
            return
        }
    }

    private class DeleteNoteAsyncTask internal constructor(
        private val noteDao: NoteDao
    ): AsyncTask<Note, Unit, Unit>() {

        override fun doInBackground(vararg params: Note) {
            noteDao.delete(params[0])
            return
        }
    }

    private class DeleteAllNoteAsyncTask internal constructor(
        private val noteDao: NoteDao
    ): AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg units: Unit?) {
            noteDao.deleteAllNotes()
            return
        }
    }
    */
}