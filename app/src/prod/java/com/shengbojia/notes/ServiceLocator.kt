package com.shengbojia.notes

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.shengbojia.notes.data.NoteRepository
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.db.AppDatabase

/**
 * Service locator pattern to obtain [Repository]. This is the prod version.
 */
object ServiceLocator {

    private val lock = Any()
    private var database: AppDatabase? = null
    @Volatile var repository: Repository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repository ?:
            repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): Repository {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "note_database"
        )
            .build()

        return NoteRepository(
            database.noteDao()
        )
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            repository = null
        }
    }
}