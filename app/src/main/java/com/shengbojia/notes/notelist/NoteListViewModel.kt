package com.shengbojia.notes.notelist

import androidx.lifecycle.*
import com.shengbojia.notes.Event
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.Result.Success
import com.shengbojia.notes.utility.ADD_EDIT_RESULT_OK
import com.shengbojia.notes.utility.DELETE_RESULT_OK
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

    // Whether or not to display "empty notes, write some" message
    val empty: LiveData<Boolean> = Transformations.map(_notes) {
        it.isEmpty()
    }


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

    fun showEditResultMessage(userMessage: Int) {
        when (userMessage) {
            ADD_EDIT_RESULT_OK -> _snackBarText.value = Event(R.string.snackbar_success_saved)
            DELETE_RESULT_OK -> _snackBarText.value = Event(R.string.snackbar_success_delete)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            val result = repository.deleteAllNotes()
            if (result is Success) {
                _snackBarText.value = Event(R.string.snackbar_success_deleteAll)
            } else {
                _snackBarText.value = Event(R.string.snackbar_error_delete)
            }
            getAllNotes(false)
        }
    }

    fun getAllNotes(showSnackbarMsg: Boolean) {
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

                if (showSnackbarMsg) {
                    _snackBarText.value = Event(R.string.snackbar_error_loading)
                }
            }

            _dataLoading.value = false
        }
    }
}