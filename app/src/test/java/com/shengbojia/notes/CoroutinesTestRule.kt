package com.shengbojia.notes

import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.Executors
import kotlin.coroutines.ContinuationInterceptor

/**
 * Sets the main coroutines dispatcher for unit testing.
 *
 * Uses the deprecated TestCoroutineContext if provided. Otherwise it uses a new single thread
 * executor.
 * See https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471
 * and https://github.com/Kotlin/kotlinx.coroutines/issues/541
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ViewModelScopeMainDispatcherRule(
    private val testContext: TestCoroutineContext? = null
) : TestWatcher() {

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun starting(description: Description?) {
        super.starting(description)
        if (testContext != null) {
            Dispatchers.setMain(testContext[ContinuationInterceptor] as CoroutineDispatcher)
        } else {
            Dispatchers.setMain(singleThreadExecutor.asCoroutineDispatcher())
        }
    }

    override fun finished(description: Description?) {
        super.finished(description)
        singleThreadExecutor.shutdownNow()
        Dispatchers.resetMain()
    }
}