package com.shengbojia.notes.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask

class NoteRepository private constructor(application: Application){
    private var noteDao: NoteDao = NoteDatabase.getInstance(application).noteDao()

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNoteAsyncTask(noteDao).execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(application: Application): NoteRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteRepository(application).also { INSTANCE = it }
            }
    }

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
}