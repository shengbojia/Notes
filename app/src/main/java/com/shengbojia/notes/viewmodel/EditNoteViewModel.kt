package com.shengbojia.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * [ViewModel] used in
 */
class EditNoteViewModel internal constructor(
    private val repository: NoteRepository
) : ViewModel() {

    /**
     * Cancels all coroutines when ViewModel is cleared.
     */
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

    fun saveEditedNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}