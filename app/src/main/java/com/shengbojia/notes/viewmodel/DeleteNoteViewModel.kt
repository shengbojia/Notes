package com.shengbojia.notes.viewmodel

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
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

    val notesToDelete = hashSetOf<Note>()


    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun deleteNotes() {
        viewModelScope.launch {
            for (note in notesToDelete) {
                repository.delete(note)
            }
        }
    }
}