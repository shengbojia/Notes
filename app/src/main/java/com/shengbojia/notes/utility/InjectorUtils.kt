package com.shengbojia.notes.utility

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.shengbojia.notes.adapter.NoteAdapter
import com.shengbojia.notes.data.AppDatabase
import com.shengbojia.notes.data.NoteRepository
import com.shengbojia.notes.ui.actionmode.MainActionModeCallback
import com.shengbojia.notes.viewmodel.*

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getNoteRepository(context: Context): NoteRepository =
            NoteRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).noteDao()
            )

    fun provideAddNoteViewModelFactory(context: Context): AddNoteViewModelFactory {
        val repository = getNoteRepository(context)
        return AddNoteViewModelFactory(repository)
    }

    fun provideEditNoteViewModelFactory(context: Context, noteId: Int): EditNoteViewModelFactory {
        val repository = getNoteRepository(context)
        return EditNoteViewModelFactory(repository, noteId)
    }

    fun provideNoteListViewModelFactory(context: Context): NoteListViewModelFactory {
        val repository = getNoteRepository(context)
        return NoteListViewModelFactory(repository)
    }

    fun provideDeleteNoteViewModelFactory(context: Context): DeleteNoteViewModelFactory {
        val repository = getNoteRepository(context)
        return DeleteNoteViewModelFactory(repository)
    }

    fun provideAdapterWithActionMode(activity: AppCompatActivity, viewModel: DeleteNoteViewModel): NoteAdapter {

        val actionModeCallback = MainActionModeCallback(viewModel, activity)
        val adapter = NoteAdapter(actionModeCallback)
        actionModeCallback.adapter = adapter

        return adapter
    }
}