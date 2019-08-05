package com.shengbojia.notes.notelist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.shengbojia.notes.MainActivity
import com.shengbojia.notes.R
import com.shengbojia.notes.ServiceLocator
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.Repository
import com.shengbojia.notes.utility.insertNoteBlocking
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

/**
 * Integration test for the note list title screen.
 */

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class NoteListFragmentTest {

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
    fun clickOnAddNoteFab_navigateToAddEditFragment() {
        // Given - start on home note list screen
        val scenario = launchFragmentInContainer<NoteListFragment>(
            Bundle(),
            R.style.AppTheme)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            // associate the mock nav controller with the fragment
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When the FAB to add note is clicked
        onView(withId(R.id.fab_noteList_newNote)).perform(click())

        // Then verify the correct navigation call to add screen is made
        verify(navController).navigate(
            NoteListFragmentDirections.actionNoteListFragmentToAddEditNoteFragment(
                null, getApplicationContext<Context>().getString(R.string.fragment_title_addNote)
            )
        )

    }

    @Test
    fun displayNoteList_whenRepositoryNotEmpty() {
        // Given one note already in the repository
        repository.insertNoteBlocking(Note("title1", "desc1"))

        // When starting up app
        launch(MainActivity::class.java)

        // Then verify the note is visibly displayed on screen
        onView(withText("title1")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteOneNote() {
        repository.insertNoteBlocking(Note("title1", "desc1"))

        launch(MainActivity::class.java)

        // Open the details of the inserted note
        onView(withText("title1")).perform(click())

        // Click the delete icon on the action bar
        onView(withId(R.id.action_delete)).perform(click())

        // Then verify that the note was deleted
        onView(withText("title1")).check(doesNotExist())

    }
}