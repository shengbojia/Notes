package com.shengbojia.notes.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository.getInstance(application)

    fun insert(note: Note) {
        repository.insert(note)
    }

}