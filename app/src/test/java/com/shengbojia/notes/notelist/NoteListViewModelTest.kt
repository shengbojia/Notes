package com.shengbojia.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shengbojia.notes.ViewModelScopeMainDispatcherRule
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.data.NoteRepository
import com.shengbojia.notes.data.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock

class NoteListViewModelTest {

    // View model to be tested
    private lateinit var noteListViewModel: NoteListViewModel

    // Mock repository
    private val mockRepository: Repository = mock(Repository::class.java)

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
        val note1 = Note("Title1", "Desc1")
        val note2 = Note("Title2", "Desc2")
        val note3 = Note("Title3", "Desc3")

        mockRepository.insert()
    }

    @After
    fun tearDown() {
    }
}