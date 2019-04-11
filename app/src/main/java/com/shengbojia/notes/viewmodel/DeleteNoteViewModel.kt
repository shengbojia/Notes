package com.shengbojia.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DeleteNoteViewModel internal constructor(
    private val repository: NoteRepository
) : ViewModel() {



    private val selectedNotes = emptyList<Note>()

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun deleteNotes(notesToDelete: List<Note>) {
        viewModelScope.launch {
            for (note in notesToDelete) {
                repository.delete(note)
            }
        }
    }
}