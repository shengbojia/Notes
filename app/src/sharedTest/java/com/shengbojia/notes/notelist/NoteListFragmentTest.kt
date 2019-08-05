package com.shengbojia.notes.notelist

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Repository
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Integration test for the note list title screen.
 */

@RunWith(AndroidJUnit4::class)
class NoteListFragmentTest {

    private lateinit var repository: Repository

    @Before
    fun setupRepository() {
        repository = FakeRepository()
    }


}