package com.shengbojia.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.Event
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.Result
import com.shengbojia.notes.data.Result.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoteListViewModel internal constructor(
    private val repository: Repository
) : ViewModel() {

    // Mutable livedata for internal use, and immutable external access

    private val _notes = MutableLiveData<List<Note>>().apply { value = emptyList() }
    val notes: LiveData<List<Note>> = _notes

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    private val _openNoteEvent = MutableLiveData<Event<String>>()
    val openNoteEvent: LiveData<Event<String>> = _openNoteEvent

    private val _newNoteEvent = MutableLiveData<Event<Unit>>()
    val newNoteEvent: LiveData<Event<Unit>> = _newNoteEvent

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText


    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addNewNote() {
        _newNoteEvent.value = Event(Unit)
    }

    fun openNote(noteId: String) {
        _openNoteEvent.value = Event(noteId)
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            val result = repository.deleteAllNotes()
            if (result is Success) {
                _snackBarText.value = Event(R.string.snackbar_success_deleteAll)
            } else {
                _snackBarText.value = Event(R.string.snackbar_error_delete)
            }
            getAllNotes()
        }
    }

    fun getAllNotes() {
        _dataLoading.value = true

        viewModelScope.launch {
            val result = repository.getAllNotes()

            if (result is Success) {
                val notesList = result.data

                _isDataLoadingError.value = false
                _notes.value = notesList
            } else {
                _isDataLoadingError.value = true
                _notes.value = emptyList()

                _snackBarText.value = Event(R.string.snackbar_error_loading)
            }

            _dataLoading.value = false
        }
    }
}