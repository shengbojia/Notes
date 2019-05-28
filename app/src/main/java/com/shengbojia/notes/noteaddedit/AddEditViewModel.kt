package com.shengbojia.notes.noteaddedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengbojia.notes.Event
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.Result.Success
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class AddEditViewModel (
    private val repository: Repository
) : ViewModel() {

    // Two way databinding so user updates to title/desc are live
    val title = MutableLiveData<String>()

    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    private val _noteUpdatedEvent = MutableLiveData<Event<Unit>>()
    val noteUpdatedEvent: LiveData<Event<Unit>> = _noteUpdatedEvent

    private var noteId: String? = null

    private var isNewNote = false

    private var isDataLoaded = false

    fun start(noteId: String?) {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        this.noteId = noteId
        if (noteId == null) {

            // null id means we are creating a new note, so no need to populate UI with title/desc
            isNewNote = true
            return
        }
        if (isDataLoaded) {

            // no need to populate UI as the data has already been loaded in
            return
        }

        isNewNote = false
        _dataLoading.value = true

        viewModelScope.launch {
            repository.getNote(noteId).let { result ->
                if (result is Success) {
                    onNoteDataLoaded(result.data)
                } else {
                    onNoteDataUnavailable()
                }
            }
        }

    }

    private fun onNoteDataLoaded(note: Note) {
        title.value = note.title
        description.value = note.description
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onNoteDataUnavailable() {
        _dataLoading.value = false
    }

    fun saveNote() {
        val currentTitle = title.value
        val currentDescription = description.value

        if (currentTitle == null || currentDescription == null) {
            _snackBarText.value = Event(R.string.snackbar_message_cannotBeEmpty)
            return
        }
        if (Note(currentTitle, currentDescription).isEmpty()) {
            _snackBarText.value = Event(R.string.snackbar_message_cannotBeEmpty)
            return
        }

        val currentNoteId = noteId

        if (isNewNote || currentNoteId == null) {
            saveNewNote(Note(currentTitle, currentDescription))
        } else {
            val noteToUpdate = Note(currentTitle, currentDescription, currentNoteId)
            updateExistingNote(noteToUpdate)
        }
    }

    private fun saveNewNote(newNote: Note) = viewModelScope.launch {
        val result = repository.insert(newNote)
        if (result is Success) {
            _noteUpdatedEvent.value = Event(Unit)
        } else {
            _snackBarText.value = Event(R.string.snackbar_error_insert)
        }
    }

    private fun updateExistingNote(note: Note) {
        if (isNewNote) {
            throw RuntimeException("updateExistingNote() was called but suppose to be a new note.")
        }
        viewModelScope.launch {
            val result = repository.update(note)
            if (result is Success) {
                _noteUpdatedEvent.value = Event(Unit)
            } else {
                _snackBarText.value = Event(R.string.snackbar_error_insert)
            }
        }
    }
}