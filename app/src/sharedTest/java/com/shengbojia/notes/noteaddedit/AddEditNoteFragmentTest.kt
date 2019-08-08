package com.shengbojia.notes.noteaddedit

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.shengbojia.notes.R
import com.shengbojia.notes.ServiceLocator
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.data.Result
import com.shengbojia.notes.utility.ADD_EDIT_RESULT_OK
import com.shengbojia.notes.utility.getAllNotesBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class AddEditNoteFragmentTest {

    private lateinit var repository: Repository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        // Use the testing-only setter
        ServiceLocator.repository = repository
    }

    @After
    fun cleanDatabase() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun emptyNote_isNotSaved() {
        // Given - on add note screen
        val bundle = AddEditNoteFragmentArgs(
            null,
            getApplicationContext<Context>().getString(R.string.fragment_title_addNote)).toBundle()
        launchFragmentInContainer<AddEditNoteFragment>(bundle, R.style.AppTheme)

        // When trying to save unfinished note with no text in title/description
        onView(withId(R.id.et_edit_title)).perform(clearText())
        onView(withId(R.id.et_edit_desc)).perform(clearText())
        onView(withId(R.id.fab_addEdit_save)).perform(click())

        // Then verify still on add screen since a correct would have returned to main screen
        onView(withId(R.id.et_edit_desc)).check(matches(isDisplayed()))

        // And snackbar pops up with warning message
        onView(withId(R.id.snackbar_text)).check(matches(withText(R.string.snackbar_message_cannotBeEmpty)))
    }

    @Test
    fun validNote_isSaved() {
        // Given - starting on add fragment in isolation
        val navController = mock(NavController::class.java)
        setupFragment(navController)

        // When saving a valid note with nonempty title and description
        val title = "title"
        val desc = "desc"
        onView(withId(R.id.et_edit_title)).perform(replaceText(title))
        onView(withId(R.id.et_edit_desc)).perform(replaceText(desc))
        onView(withId(R.id.fab_addEdit_save)).perform(click())

        // Then verify the note has been saved in the repository
        val notes = (repository.getAllNotesBlocking() as Result.Success).data
        assertThat(notes).hasSize(1)
        assertThat(notes[0].title).isEqualTo(title)
        assertThat(notes[0].description).isEqualTo(desc)
    }

    @Test
    fun validNoteSaved_navigatesToNoteList() {
        // Given - starting on add fragment in isolation
        val navController = mock(NavController::class.java)
        setupFragment(navController)

        // When saving a valid note with nonempty title and description
        val title = "title"
        val desc = "desc"
        onView(withId(R.id.et_edit_title)).perform(replaceText(title))
        onView(withId(R.id.et_edit_desc)).perform(replaceText(desc))
        onView(withId(R.id.fab_addEdit_save)).perform(click())

        // Then verify navigation back to note list screen
        verify(navController).navigate(
            AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNoteListFragment(ADD_EDIT_RESULT_OK)
        )
    }

    /**
     * Helper method for setting up a [AddEditNoteFragment] with a [NavController].
     */
    private fun setupFragment(navController: NavController?) {
        val bundle = AddEditNoteFragmentArgs(null,
            getApplicationContext<Context>().getString(R.string.fragment_title_addNote)).toBundle()
        val scenario = launchFragmentInContainer<AddEditNoteFragment>(bundle, R.style.AppTheme)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
    }

}