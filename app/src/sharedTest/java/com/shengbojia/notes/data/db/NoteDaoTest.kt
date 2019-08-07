package com.shengbojia.notes.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {

    private lateinit var database: AppDatabase

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun cleanDatabase() {
        database.close()
    }

    @Test
    fun insert_note_getById() = runBlockingTest {
        // Given - insert a note in the db
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - get the note by id
        val loaded = database.noteDao().getNote(note.id)

        // Then - the loaded note should match the original
        assertThat(loaded).isInstanceOf(Note::class.java)
        assertThat(loaded).isNotNull()

        assertThat(loaded?.id).isEqualTo(note.id)
        assertThat(loaded?.title).isEqualTo(note.title)
        assertThat(loaded?.description).isEqualTo(note.description)
        assertThat(loaded?.dateWritten).isEquivalentAccordingToCompareTo(note.dateWritten)
    }

    @Test
    fun insert_sameNote_replacesOnConflict() = runBlockingTest {

        // Given - insert a note
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - note with same id is inserted
        val newNote = Note("title2", "description2", note.id)
        database.noteDao().insert(newNote)

        // Then - newNote should have replaced note
        val loaded = database.noteDao().getNote(note.id)
        assertThat(loaded?.title).isEqualTo("title2")
        assertThat(loaded?.description).isEqualTo("description2")
        assertThat(loaded?.id).isEqualTo(note.id)
    }

    @Test
    fun insert_note_getAllNotes() = runBlockingTest {
        // Given - insert a note
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - get all notes
        val noteList = database.noteDao().getAllNotes()

        // Then - only 1 note in db and should be the inserted note
        assertThat(noteList.size).isEqualTo(1)
        assertThat(noteList[0].title).isEqualTo("title")
        assertThat(noteList[0].description).isEqualTo("description")
        assertThat(noteList[0].id).isEqualTo(note.id)
    }

    @Test
    fun update_note_getNoteById() = runBlockingTest {
        // Given - insert a note
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - the note is updated
        val updatedNote = Note("title2", "description2", note.id)
        database.noteDao().update(updatedNote)

        // Then - the loaded data should contain the same values
        val loaded = database.noteDao().getNote(note.id)
        assertThat(loaded?.title).isEqualTo("title2")
        assertThat(loaded?.description).isEqualTo("description2")
        assertThat(loaded?.id).isEqualTo(note.id)
    }

    @Test
    fun delete_noteById_getAllNotes() = runBlockingTest {

        // Given - insert a note
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - delete the note by id
        database.noteDao().delete(note.id)

        // Then - list of all notes should be empty
        val noteList = database.noteDao().getAllNotes()
        assertThat(noteList).isEmpty()
    }

    @Test
    fun deleteAllNotes_getAllNotes_dbEmpty() = runBlockingTest {
        // Given - insert a note
        val note = Note("title", "description")
        database.noteDao().insert(note)

        // When - delete all notes
        database.noteDao().deleteAllNotes()

        // Then - list of notes should be empty
        val noteList = database.noteDao().getAllNotes()
        assertThat(noteList).isEmpty()
    }
}