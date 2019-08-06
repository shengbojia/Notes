package com.shengbojia.notes.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shengbojia.notes.data.db.AppDatabase
import com.shengbojia.notes.data.Result.Success
import com.google.common.truth.Truth.*
import com.shengbojia.notes.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented unit test for [NoteRepository].
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class NoteRepositoryTest {

    private lateinit var repository: NoteRepository
    private lateinit var database: AppDatabase

    // Set the main coroutine dispatcher to be the test one
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Some issues with runBlockTest. According this https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // A quick fix is to add this rule
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = NoteRepository(database.noteDao(), Dispatchers.Main) // different dispatcher for testing
    }

    @After
    fun cleanDatabase() {
        database.close()
    }

    @Test
    fun insert_resultSuccess() = runBlockingTest {
        // Given - note
        val note = Note("title", "description")

        // When - insert the note
        val result = repository.insert(note)

        // Then - result is success
        assertThat(result.succeeded).isTrue()
    }


    @Test
    fun insert_getById() = runBlockingTest {
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
    fun insert_getAllNotes() = runBlockingTest {
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

    @Test
    fun update_resultSuccess() = runBlockingTest {
        // Given - existing note in db
        val note = Note("title", "description")
        repository.insert(note)

        // When updating with a new note that has the same primary key
        val newNote = Note("newTitle", "newDesc", note.id)
        val result = repository.update(newNote)

        // Then - result is success of 1
        assertThat(result.succeeded).isTrue()
        result as Success
        assertThat(result.data).isInstanceOf(Integer::class.java)
        assertThat(result.data).isEqualTo(1)
    }

    @Test
    fun update_valueChanges() = runBlockingTest {
        // Given - existing note in db
        val note = Note("title", "description")
        repository.insert(note)

        // When updating with a new note that has the same primary key
        val newNote = Note("newTitle", "newDesc", note.id)
        repository.update(newNote)

        // Then verify the note in the db has updated title and desc, and same time stamp as updated note
        val result = repository.getNote(note.id)
        result as Success
        assertThat(result.data.title).isEqualTo("newTitle")
        assertThat(result.data.description).isEqualTo("newDesc")
        assertThat(result.data.id).isEqualTo(note.id)
        assertThat(result.data.dateWritten).isEquivalentAccordingToCompareTo(newNote.dateWritten)
    }


}