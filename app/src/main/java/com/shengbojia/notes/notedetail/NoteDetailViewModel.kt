package com.shengbojia.notes.notedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.Event
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.Result.Success
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editNoteCommand = MutableLiveData<Event<Unit>>()
    val editNoteCommand: LiveData<Event<Unit>> = _editNoteCommand

    private val _deleteNoteCommand = MutableLiveData<Event<Unit>>()
    val deleteNoteCommand: LiveData<Event<Unit>> = _deleteNoteCommand

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    val noteId: String?
        get() = _note.value?.id

    fun deleteNote() = viewModelScope.launch {
        noteId?.let {
            val result = repository.delete(it)
            if (result is Success) {
                _deleteNoteCommand.value = Event(Unit)
            } else {
                _snackbarText.value = Event(R.string.snackbar_error_delete)
            }
        }
    }

    fun editNote() {
        _editNoteCommand.value = Event(Unit)
    }

    fun start(noteId: String?) = viewModelScope.launch {
        if (noteId != null) {
            _dataLoading.value = true
            repository.getNote(noteId).let { result ->
                if (result is Success) {
                    onNoteDataLoaded(result.data)
                } else {
                    onNoteDataUnavailable()
                }
            }
        }
    }

    private fun setNote(note: Note?) {
        _note.value = note
        _isDataAvailable.value = note != null
    }

    private fun onNoteDataLoaded(note: Note) {
        setNote(note)
        _dataLoading.value = false
    }

    private fun onNoteDataUnavailable() {
        _note.value = null
        _dataLoading.value = false
        _isDataAvailable.value = false
    }
}
