package com.shengbojia.notes.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.shengbojia.notes.data.db.AppDatabase
import com.shengbojia.notes.data.Result.Success
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Instrumented unit test for [NoteRepository].
 */
class NoteRepositoryTest {

    private lateinit var repository: NoteRepository
    private lateinit var database: AppDatabase


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = NoteRepository(database.noteDao())
    }

    @After
    fun cleanDatabase() {
        database.close()
    }

    @Test
    fun insert_success_resultSuccess() = runBlocking {
        // Given - note
        val note = Note("title", "description")

        // When - insert the note
        val result = repository.insert(note)
        //result as Result.Error
        //assertThat(result.ex.message).isEqualTo("")
        // Then - result is success

        assertThat(result.succeeded).isTrue()
    }


    @Test
    fun insertNote_getById() = runBlocking {
        // Given - insert a note
        val note = Note("title", "description")
        repository.insert(note)

        // When - get the note by id
        val result = repository.getNote(note.id)

        // Then - result is success and holds the note
        assertThat(result.succeeded).isTrue()
        result as Success
        assertThat(result.data.title).isEqualTo("title")
        assertThat(result.data.description).isEqualTo("description")
        assertThat(result.data.id).isEqualTo(note.id)
        assertThat(result.data.dateWritten).isEquivalentAccordingToCompareTo(note.dateWritten)
    }


    @Test
    fun insertNote_getAllNotes() = runBlocking {
        // Given - insert 2 notes
        val note1 = Note("title", "description")
        val note2 = Note("title2", "description2")
        repository.insert(note1)
        repository.insert(note2)

        // When getting all notes in db
        val result = repository.getAllNotes()

        // Then result should be of size 2
        assertThat(result.succeeded).isTrue()
        result as Success
        assertThat(result.data.size).isEqualTo(2)
    }

}