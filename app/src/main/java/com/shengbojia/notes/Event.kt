package com.shengbojia.notes

import androidx.lifecycle.Observer

open class Event<out T>(
    private val content: T
) {

    // Can read but not set
    var hasBeenUsed = false
        private set

    fun getContentIfNotUsed(): T? {
        return if (hasBeenUsed) {
            null
        } else {
            hasBeenUsed = true
            content
        }
    }

    fun peekContent(): T = content
}

/**
 * [Observer] for [Event]s, simplifies the process of checking if the event's contents have been used.
 */
class EventObserver<T>(
    private val onEventHasUnusedConten: (T) -> Unit
) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotUsed()?.let {
            onEventHasUnusedConten(it)
        }
    }
}

