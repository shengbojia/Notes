package com.shengbojia.notes.noteaddedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shengbojia.notes.MainCoroutineRule
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditViewModelTest {

    // View model to be tested
    private lateinit var viewModel: AddEditViewModel

    // Fake repository, I didn't use a mock here because there's some details to work out
    private lateinit var fakeRepository: FakeRepository

    // Set the main coroutine dispatcher to be the test one
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Swaps background executor used by the Architecture Components with a different
    // one that executes each task synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // primary note instance for testing
    private val note = Note("title", "desc")

    @Before
    fun setupAddEditViewModel() {
        // Setup a fresh repo before every test
        fakeRepository = FakeRepository()

        // create the viewmodel to be tested
        viewModel = AddEditViewModel(fakeRepository)
    }

    @Test
    fun start_loadingNoteData() {
        // Pause dispatcher to verify initial values
        mainCoroutineRule.pauseDispatcher()

        // load the note in viewmodel
        viewModel.start(note.id)

        // Then data loading indicator is shown


    }
}