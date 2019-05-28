package com.shengbojia.notes.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.noteaddedit.AddEditViewModel
import com.shengbojia.notes.notelist.NoteListViewModel
import java.lang.IllegalArgumentException


/**
 * Class to inject various [ViewModel] classes.
 */
class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(AddEditViewModel::class.java) ->
                    AddEditViewModel(repository)
                isAssignableFrom(NoteListViewModel::class.java) ->
                    NoteListViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown viewmodel class: ${modelClass.name}")
            }
        } as T
}
