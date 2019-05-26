package com.shengbojia.notes.data.db

import androidx.room.Room
import org.junit.runner.RunWith
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.*
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

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
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertNoteAndGetById() = runBlocking {
        // Given - insert a note in the db
        val note = Note(title = "title", description = "description")
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
}