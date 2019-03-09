package com.shengbojia.notes.utility

import android.content.Context
import com.shengbojia.notes.data.AppDatabase
import com.shengbojia.notes.data.NoteRepository

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getNoteRepository(context: Context): NoteRepository =
            NoteRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).noteDao()
            )
}