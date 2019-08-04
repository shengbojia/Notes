package com.shengbojia.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.shengbojia.notes.LiveDataTestUtil
import com.shengbojia.notes.R
import com.shengbojia.notes.ViewModelScopeMainDispatcherRule
import com.shengbojia.notes.assertSnackbarMessage
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteListViewModelTest {

    // View model to be tested
    private lateinit var viewModel: NoteListViewModel

    // Fake repository, I didn't use a mock here because there's some details to work out
    private lateinit var fakeRepository: FakeRepository

    // CoroutineContext that can be controlled during tests
    private val testContext = TestCoroutineContext()

    // Set the main coroutine dispatcher to be the test one
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineMainDispatcherRule = ViewModelScopeMainDispatcherRule(testContext)

    // Swaps background executor used by the Architecture Components with a different
    // one that executes each task synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUpNoteListViewModel() {

        // Setup a fresh repo before every test
        fakeRepository = FakeRepository()

        val note1 = Note("Title1", "Desc1")
        val note2 = Note("Title2", "Desc2")
        val note3 = Note("Title3", "Desc3")

        fakeRepository.addNotes(note1, note2, note3)

        viewModel = NoteListViewModel(fakeRepository)
    }

    @Test
    fun getAllNotesFromRepository_dataLoadingTogglesAndDataLoaded() {
        // Given an initialized NoteListViewModel and initialized + inserted notes

        // When loading of all notes is requested
        viewModel.getAllNotes()

        // Then data loading indicator is shown
        assertThat(LiveDataTestUtil.getValue(viewModel.dataLoading)).isTrue()

        // Execute pending coroutine actions
        testContext.triggerActions()

        // Then data loading indicator becomes hidden
        assertThat(LiveDataTestUtil.getValue(viewModel.dataLoading)).isFalse()

        // And data was correctly loaded
        assertThat(LiveDataTestUtil.getValue(viewModel.notes)).hasSize(3)
    }

    @Test
    fun getAllNotesFromRepository_error() {
        // Given repository that will return an error
        fakeRepository.setShouldReturnError(true)

        // When loading of all notes is requested
        viewModel.getAllNotes()

        // Execute pending coroutine actions
        testContext.triggerActions()

        // Then data loading error indicator is true
        assertThat(LiveDataTestUtil.getValue(viewModel.isDataLoadingError)).isTrue()

        // And list of notes is empty
        assertThat(LiveDataTestUtil.getValue(viewModel.notes)).isEmpty()

        // And snackbar text is updated with correct error message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_loading)
    }
}