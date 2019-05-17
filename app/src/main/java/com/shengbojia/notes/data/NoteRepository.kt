package com.shengbojia.notes.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.shengbojia.notes.data.Result.Success
import com.shengbojia.notes.data.Result.Error


/**
 * [Repository] for handling data operations.
 */
class NoteRepository private constructor(
    private val noteDao: NoteDao
) : Repository {

    /**
     * Insert the specified note into the data table.
     *
     * @param note the note to be inserted
     * @return a [Success] of true if successful, [Error] otherwise
     */
    @WorkerThread
    override suspend fun insert(note: Note): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            noteDao.insert(note)
            return@withContext Success<Boolean>(true)
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }

    /**
     * Update the specified note. If other than 1 row is affected or an exception occurs, returns an [Error].
     *
     * @param note the note to be updated in the table
     * @return a [Success] of 1 if successful, [Error] otherwise
     */
    @WorkerThread
    override suspend fun update(note: Note): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val rowsAffected = noteDao.update(note)
            when (rowsAffected) {
                1 -> return@withContext Success<Int>(rowsAffected)
                0 -> return@withContext Error(Exception("Nothing updated."))
                else -> return@withContext Error(Exception("More than one row updated."))
            }
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }

    /**
     * Delete a note by its id. If other than 1 row is affected or an exception occurs, returns an [Error].
     *
     * @param noteId the id of the note to be deleted
     * @return a [Success] of 1 if successful, [Error] otherwise
     */
    @WorkerThread
    override suspend fun delete(noteId: Int): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val rowsAffected = noteDao.delete(noteId)
            when (rowsAffected) {
                1 -> return@withContext Success<Int>(rowsAffected)
                0 -> return@withContext Error(Exception("Nothing deleted."))
                else -> return@withContext Error(Exception("More than one row deleted."))
            }
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }

    /**
     * Delete all notes in the table. If no rows are affected or an exception occurs, returns an [Error].
     *
     * @return a [Success] of the total number of rows affected, [Error] otherwise
     */
    @WorkerThread
    override suspend fun deleteAllNotes(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val rowsAffected = noteDao.deleteAllNotes()
            when (rowsAffected) {
                0 -> return@withContext Error(Exception("Nothing deleted."))
                else -> return@withContext Success<Int>(rowsAffected)
            }
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }


    /**
     * Select a note by id. If resulting note is null or an exception occurs, return [Error].
     *
     * @param noteId the id of the note to select
     * @return [Success] of selected note, [Error] otherwise
     */
    @WorkerThread
    override suspend fun getNote(noteId: Int): Result<Note> = withContext(Dispatchers.IO) {
        try {
            val result = noteDao.getNote(noteId)
            if (result != null) {
                return@withContext Success<Note>(result)
            } else {
                return@withContext Error(Exception("Note not found!"))
            }
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }

    /**s
     * Select all notes in the data table. If an exception occurs, returns [Error].
     *
     * @return [Success] of list of [Note]s, [Error] otherwise
     */
    @WorkerThread
    override suspend fun getAllNotes(): Result<List<Note>> = withContext(Dispatchers.IO) {
        try {
            return@withContext Success<List<Note>>(noteDao.getAllNotes())
        } catch (ex: Exception) {
            return@withContext Error(ex)
        }
    }

    // Singleton instantiation
    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(noteDao: NoteDao): NoteRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteRepository(noteDao).also { INSTANCE = it }
            }
    }

}