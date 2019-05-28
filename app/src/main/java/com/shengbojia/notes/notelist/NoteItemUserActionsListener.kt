package com.shengbojia.notes.notelist

import android.view.View
import com.shengbojia.notes.data.Note

interface NoteItemUserActionsListener {
    fun onNoteClicked(note: Note)

    fun onNoteLongClicked(note: Note): Boolean
}