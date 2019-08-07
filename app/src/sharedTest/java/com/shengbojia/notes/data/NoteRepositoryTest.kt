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
 * Instrumented unit test for [NoteRepository]. Can be run on Robolectric too thought.
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
    fun getNote_emptyDb_resultError() = runBlockingTest {
        // Given empty db

        // When trying to get a note by id that is not in db
        val note = Note("not in db", "not in db")
        val result = repository.getNote(note.id)

        // Then the result will be an error: 'Note not found!'
        assertThat(result.succeeded).isFalse()
        result as Result.Error
        assertThat(result.ex.message).isEqualTo("Note not found!")
    }

    @Test
    fun getNote_wrongId_resultError() = runBlockingTest {
        // Given db with multiple notes
        repository.insert(Note("title1", "desc1"))
        repository.insert(Note("title2", "desc2"))

        // When trying to get a note by id that is not in db
        val note = Note("not in db", "not in db")
        val result = repository.getNote(note.id)

        // Then the result will be an error: 'Note not found!'
        assertThat(result.succeeded).isFalse()
        result as Result.Error
        assertThat(result.ex.message).isEqualTo("Note not found!")
    }

    @Test
    fun getAllNotes_emptyDb_resultEmpty() = runBlockingTest {
        // Given an empty db

        // When getting all notes
        val result = repository.getAllNotes()

        // Then the result is success
        assertThat(result.succeeded).isTrue()
        result as Success

        // But will contain an empty list
        assertThat(result.data).isEmpty()
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

    @Test
    fun delete_resultSuccess_dbEmpty() = runBlockingTest {
        // Given db with an existing note
        val note = Note("title", "desc")
        repository.insert(note)

        // When - delete the note by its id
        val result = repository.delete(note.id)

        // Then verify the result is a success of 1
        assertThat(result.succeeded).isTrue()
        result as Success
        assertThat(result.data).isEqualTo(1)

        // and db now empty
        assertThat((repository.getAllNotes() as Success).data).isEmpty()
    }

    @Test
    fun delete_oneNoteFromMultiple_onlyOneGetsDeleted() = runBlockingTest {
        // Given - db with multiple notes
        val noteToDel = Note("del title", "del desc")
        repository.insert(Note("title1", "desc1"))
        repository.insert(Note("title2", "desc2"))
        repository.insert(noteToDel)

        // When deleting a specific note by id
        val result = repository.delete(noteToDel.id)

        // Then verify only 1 data table row is affected
        assertThat(result.succeeded).isTrue()
        result as Success
        assertThat(result.data).isEqualTo(1)

        // and other two notes are still there
        assertThat((repository.getAllNotes() as Success).data).hasSize(2)
    }

    @Test
    fun delete_fromEmptyDb_resultError() = runBlockingTest {
        // Given empty db with no notes

        // When attempting to delete a note
        val note = Note("title", "desc")
        val result = repository.delete(note.id)

        // Then the result should be an error saying 'Nothing deleted'
        assertThat(result.succeeded).isFalse()
        result as Result.Error
        assertThat(result.ex.message).isEqualTo("Nothing deleted.")
    }

    @Test
    fun delete_noteNotInDb_resultError() = runBlockingTest {
        // Given db with multiple notes
        repository.insert(Note("title1", "desc1"))
        repository.insert(Note("title2", "desc2"))

        // When trying to delete a note that is not in db
        val noteToDel = Note("del title", "del desc")
        val result = repository.delete(noteToDel.id)

        // Then the result should be an error saying 'Nothing deleted'
        assertThat(result.succeeded).isFalse()
        result as Result.Error
        assertThat(result.ex.message).isEqualTo("Nothing deleted.")
    }

    @Test
    fun deleteAllNotes_fromEmptyDb_resultError() = runBlockingTest {
        // Given empty db with no notes

        // When trying to delete all notes
        val result = repository.deleteAllNotes()

        // Then the result should be an error saying 'Nothing deleted'
        assertThat(result.succeeded).isFalse()
        result as Result.Error
        assertThat(result.ex.message).isEqualTo("Nothing deleted.")
    }

    @Test
    fun deleteAllNotes_twoNotes_dbEmpty() = runBlockingTest {
        // Given db with multiple notes
        repository.insert(Note("title1", "desc1"))
        repository.insert(Note("title2", "desc2"))

        // When deleting all notes
        val result = repository.deleteAllNotes()

        // Then the result should be a success
        assertThat(result.succeeded).isTrue()
        result as Success

        // And the number of table rows affected should be 2
        assertThat(result.data).isEqualTo(2)
    }

}