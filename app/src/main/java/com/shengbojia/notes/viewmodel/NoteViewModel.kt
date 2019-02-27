package com.shengbojia.notes.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository(application)
    private val allNotes = repository.getAllNotes()

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}