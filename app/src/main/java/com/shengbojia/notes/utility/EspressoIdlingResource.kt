package com.shengbojia.notes.utility

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Contains a static reference to [CountingIdlingResource], only available in the 'mock' build type. Determines idleness
 * by maintaining a counter. When the counter is 0 - it is considered to be idle, when it is non-zero it is not idle
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}