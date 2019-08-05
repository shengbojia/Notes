package com.shengbojia.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.shengbojia.notes.*
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.utility.ADD_EDIT_RESULT_OK
import com.shengbojia.notes.utility.DELETE_RESULT_OK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext

import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Local unit test using a [FakeRepository] for [NoteListViewModel].
 */
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
    fun getAllNotes_dataLoadingTogglesAndDataLoaded() {
        // Given an initialized NoteListViewModel and initialized + inserted notes

        // When loading of all notes is requested
        viewModel.getAllNotes(true)

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
    fun getAllNotes_error() {
        // Given repository that will return an error
        fakeRepository.setShouldReturnError(true)

        // When loading of all notes is requested
        viewModel.getAllNotes(true)

        // Execute pending coroutine actions
        testContext.triggerActions()

        // Then data loading error indicator is true
        assertThat(LiveDataTestUtil.getValue(viewModel.isDataLoadingError)).isTrue()

        // And list of notes is empty
        assertThat(LiveDataTestUtil.getValue(viewModel.notes)).isEmpty()

        // And snackbar text is updated with correct error message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_loading)
    }

    @Test
    fun clickOnFab_addNoteEventTriggered() {
        // When clicking on the fab to add a new note
        viewModel.addNewNote()

        // Then the add note event is triggered
        val value = LiveDataTestUtil.getValue(viewModel.newNoteEvent)
        assertThat(value.getContentIfNotUsed()).isNotNull()
    }

    @Test
    fun clickOnOpenNote_openNoteEventTriggered() {
        // When clicking on a note to open it
        val noteId = "11"
        viewModel.openNote(noteId)

        // Then the open note event is triggered
        assertLiveDataEventTriggered(viewModel.openNoteEvent, noteId)
    }

    @Test
    fun deleteAllNotes_clearsNotes() = runBlocking {
        // When option to delete all notes is selected
        viewModel.deleteAllNotes()

        // load all notes
        viewModel.getAllNotes(false)

        // Execute pending coroutine actions
        testContext.triggerActions()

        // Get all notes
        val allNotes = LiveDataTestUtil.getValue(viewModel.notes)

        // Then there should be no more notes in the repo
        assertThat(allNotes).isEmpty()

        // And the snackbar is updated with correct message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_success_deleteAll)
    }

    @Test
    fun deleteAllNotes_error() = runBlocking {

        // Given repository that will cause an error when trying to delete notes
        fakeRepository.setShouldReturnError(true)

        // When told to delete all notes
        viewModel.deleteAllNotes()

        // Execute pending coroutine actions
        testContext.triggerActions()

        // Then snackbar is updated with correct error message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_delete)

    }

    @Test
    fun showEditResultMessage_addEditOk_snackbarUpdated() {
        // When the viewmodel receives a 'add edit ok' result from another fragment
        viewModel.showEditResultMessage(ADD_EDIT_RESULT_OK)

        // Then the snackbar is updated with the correct message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_success_saved)
    }

    @Test
    fun showEditResultMessage_deleteOk_snackbarUpdated() {
        // When the viewmodel receives a 'delete ok' result from another fragment
        viewModel.showEditResultMessage(DELETE_RESULT_OK)

        // Then the snackbar is updated with the correct message
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_success_delete)
    }





}