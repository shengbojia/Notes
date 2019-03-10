package com.shengbojia.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddNoteViewModel internal constructor(
    private val repository: NoteRepository
) : ViewModel() {

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun saveNewNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }
}