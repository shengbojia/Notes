package com.shengbojia.notes.utility

import android.content.Context
import com.shengbojia.notes.data.NoteRepository
import com.shengbojia.notes.data.db.AppDatabase

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun getNoteRepository(context: Context): NoteRepository =
            NoteRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).noteDao()
            )
}