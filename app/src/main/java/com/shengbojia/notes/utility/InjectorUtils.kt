package com.shengbojia.notes.utility

import android.content.Context
import com.shengbojia.notes.data.AppDatabase
import com.shengbojia.notes.data.NoteRepository
import com.shengbojia.notes.viewmodel.EditNoteViewModelFactory
import com.shengbojia.notes.viewmodel.NoteListViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getNoteRepository(context: Context): NoteRepository =
            NoteRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).noteDao()
            )

    fun provideEditNoteViewModelFactory(context: Context): EditNoteViewModelFactory {
        val repository = getNoteRepository(context)
        return EditNoteViewModelFactory(repository)
    }

    fun provideNoteListViewModelFactory(context: Context): NoteListViewModelFactory {
        val repository = getNoteRepository(context)
        return NoteListViewModelFactory(repository)
    }
}