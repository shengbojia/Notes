package com.shengbojia.notes.noteaddedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.shengbojia.notes.LiveDataTestUtil.getValue
import com.shengbojia.notes.MainCoroutineRule
import com.shengbojia.notes.R
import com.shengbojia.notes.assertSnackbarMessage
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Result
import com.shengbojia.notes.utility.getAllNotesBlocking
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
    fun start_loadNote_dataLoadingToggles() {
        // Pause dispatcher to verify initial values
        mainCoroutineRule.pauseDispatcher()

        // load the note in viewmodel
        viewModel.start(note.id)

        // Then data loading indicator is shown
        assertThat(getValue(viewModel.dataLoading)).isTrue()

        // Execute pending coroutine actions
        mainCoroutineRule.resumeDispatcher()

        // Then the data loading indicator is hidden
        assertThat(getValue(viewModel.dataLoading)).isFalse()
    }

    @Test
    fun start_loadNote_noteIsShown() {
        // Given a note in the repository
        fakeRepository.addNotes(note)

        // When loading the note
        viewModel.start(note.id)

        // then verify the viewmodel correctly loads the note
        assertThat(getValue(viewModel.title)).isEqualTo("title")
        assertThat(getValue(viewModel.description)).isEqualTo("desc")
        assertThat(getValue(viewModel.dataLoading)).isFalse()
    }

    @Test
    fun start_loadNote_errorNotFound() {
        fakeRepository.setShouldReturnError(true)

        // load the note
        viewModel.start(note.id)

        // verify data loading indicator is hidden
        assertThat(getValue(viewModel.dataLoading)).isFalse()

        // and snackbar has correct error
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_noteMissing)
    }

    @Test
    fun saveNote_newNote_noteSaved() {
        val newTitle = "new title"
        val newDesc = "new desc"

        viewModel.apply {
            title.value = newTitle
            description.value = newDesc
        }
        viewModel.saveNote()

        val notes = (fakeRepository.getAllNotesBlocking() as Result.Success).data
        assertThat(notes).hasSize(1)
        assertThat(getValue(viewModel.noteUpdatedEvent).peekContent()).isEqualTo(Unit)
    }

    @Test
    fun saveNote_newNote_errorSaving() {
        // make repo return error
        fakeRepository.setShouldReturnError(true)

        val newTitle = "new title"
        val newDesc = "new desc"
        viewModel.apply {
            title.value = newTitle
            description.value = newDesc
        }
        viewModel.saveNote()

        // then saving error
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_insert)
    }

    @Test
    fun saveNote_updatingNote_noteSaved() {
        fakeRepository.addNotes(note)

        viewModel.start(note.id)

        // edit and try to save the note
        val newTitle = "new title"
        val newDesc = "new desc"
        viewModel.apply {
            title.value = newTitle
            description.value = newDesc
        }
        viewModel.saveNote()

        // then the note  should be updated in repo
        val notes = (fakeRepository.getAllNotesBlocking() as Result.Success).data
        assertThat(notes).hasSize(1)
        assertThat(getValue(viewModel.noteUpdatedEvent).peekContent()).isEqualTo(Unit)

        assertThat(notes[0].title).isEqualTo(newTitle)
        assertThat(notes[0].description).isEqualTo(newDesc)
    }

    @Test
    fun saveNote_updatingNote_errorSaving() {
        fakeRepository.addNotes(note)

        viewModel.start(note.id)

        // edit and try to save the note
        viewModel.apply {
            title.value = "changed title"
            description.value = "changed desc"
        }

        mainCoroutineRule.pauseDispatcher()
        fakeRepository.setShouldReturnError(true)
        viewModel.saveNote()

        mainCoroutineRule.resumeDispatcher()

        // then saving error
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_error_insert)
    }

    @Test
    fun saveNote_emptyTitle_error() {
        assertUnfinishedNoteSnackbarWarning("", "Some Description")
    }

    @Test
    fun saveNote_nullTitle_error() {
        assertUnfinishedNoteSnackbarWarning(null, "Some Description")
    }
    @Test
    fun saveNote_emptyDescription_error() {
        assertUnfinishedNoteSnackbarWarning("Title", "")
    }

    @Test
    fun saveNote_nullDescription_error() {
        assertUnfinishedNoteSnackbarWarning("Title", null)
    }

    @Test
    fun saveNote_nullDescriptionNullTitle_error() {
        assertUnfinishedNoteSnackbarWarning(null, null)
    }

    @Test
    fun saveNote_emptyDescriptionEmptyTitle_error() {
        assertUnfinishedNoteSnackbarWarning("", "")
    }

    private fun assertUnfinishedNoteSnackbarWarning(title: String?, description: String?) {
        (viewModel).apply {
            this.title.value = title
            this.description.value = description
        }

        // When saving an unfinised note
        viewModel.saveNote()

        // Then the snackbar shows an error
        assertSnackbarMessage(viewModel.snackBarText, R.string.snackbar_message_cannotBeEmpty)
    }
}