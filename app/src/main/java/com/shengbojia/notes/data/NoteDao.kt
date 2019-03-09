package com.shengbojia.notes.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


/**
 * The Data Access Object for the [Note] class.
 */
@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY date_written DESC")
    fun getAllNotes(): LiveData<List<Note>>

}