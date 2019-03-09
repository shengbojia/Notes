package com.shengbojia.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shengbojia.notes.data.NoteRepository

/**
 * Factory for creating a [EditNoteViewModel] with a constructor that
 * takes a [NoteRepository].
 */
class EditNoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditNoteViewModel(repository) as T
    }
}