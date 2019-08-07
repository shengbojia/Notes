package com.shengbojia.notes.data

import androidx.annotation.VisibleForTesting
import com.google.common.collect.Lists
import com.shengbojia.notes.data.Result.Error
import com.shengbojia.notes.data.Result.Success

/**
 * Fake for [Repository]. Didn't use a mock since there is some functionality mocks and stubs can't provide.
 */
class FakeRepository : Repository {

    @VisibleForTesting
    val fakeData = LinkedHashMap<String, Note>()

    private var shouldReturnError = false

    fun setShouldReturnError(shouldReturnError: Boolean) {
        this.shouldReturnError = shouldReturnError
    }

    override suspend fun insert(note: Note): Result<Boolean> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }


        fakeData[note.id] = note

        return Success(true)
    }

    override suspend fun update(note: Note): Result<Int> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }

        fakeData[note.id] = note
        return Success(1)
    }

    override suspend fun delete(noteId: String): Result<Int> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }

        fakeData.remove(noteId)
        return Success(1)
    }

    override suspend fun deleteAllNotes(): Result<Int> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }

        val dataSetSize = fakeData.size
        fakeData.clear()
        return Success(dataSetSize)
    }

    override suspend fun getNote(noteId: String): Result<Note> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }

        fakeData[noteId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find note in fakeData"))
    }

    override suspend fun getAllNotes(): Result<List<Note>> {
        if (shouldReturnError) {
            return Error(Exception("Fake error"))
        }
        return Success(Lists.newArrayList(fakeData.values))
    }

    // function for testing that is otherwise private
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun addNotes(vararg notes: Note) {
        for (note in notes) {
            fakeData[note.id] = note
        }
    }
}