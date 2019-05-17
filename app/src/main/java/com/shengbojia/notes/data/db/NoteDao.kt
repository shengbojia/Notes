package com.shengbojia.notes.data.db

import androidx.room.*
import com.shengbojia.notes.data.Note

/**
 * The Data Access Object for the [Note] class.
 */
@Dao
interface NoteDao {

    /**
     * Insert a note into the database. If the note already exists, replace it.
     *
     * @param note the note to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    /**
     * Update a note.
     *
     * @param note the note to update
     * @return the number of notes updated. Should be 1
     */
    @Update
    fun update(note: Note): Int

    /**
     * Delete a note by id.
     *
     * @param noteId the id of the note to delete
     * @return the number of notes deleted. Should be 1
     */
    @Query("DELETE FROM note_table WHERE id = :noteId")
    fun delete(noteId: Int): Int

    /**
     * Delete all notes in the database.
     *
     * @return the total number of notes deleted
     */
    @Query("DELETE FROM note_table")
    fun deleteAllNotes(): Int

    /**
     * Select a note by id.
     *
     * @param noteId the id of the note to be selected
     * @return the selected note
     */
    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNote(noteId: Int): Note?

    /**
     * Select all notes from note_table.
     *
     * @return the list of notes
     */
    @Query("SELECT * FROM note_table ORDER BY date_written DESC")
    fun getAllNotes(): List<Note>

}