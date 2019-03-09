package com.shengbojia.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository

class AddEditNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository.getInstance(application)

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

}