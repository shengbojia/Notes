package com.shengbojia.notes.notelist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.shengbojia.notes.R
import com.shengbojia.notes.ServiceLocator
import com.shengbojia.notes.data.FakeRepository
import com.shengbojia.notes.data.Repository
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
}