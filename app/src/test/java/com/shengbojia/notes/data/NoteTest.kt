package com.shengbojia.notes.data

import org.junit.Test

/**
 * Simple local unit test for [Note].
 */
class NoteTest {

    @Test
    fun isEmpty_titleEmpty_true() {
        val note = Note("", "description")

        assert(note.isEmpty())
    }

    @Test
    fun isEmpty_descEmpty_true() {
        val note = Note("title", "")

        assert(note.isEmpty())
    }

    @Test
    fun isEmpty_bothEmpty_true() {
        val note = Note("", "")

        assert(note.isEmpty())
    }

    @Test
    fun isEmpty_bothNonEmpty_false() {
        val note = Note("title", "description")

        assert(!note.isEmpty())
    }
}