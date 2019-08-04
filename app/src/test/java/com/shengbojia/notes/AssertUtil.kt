package com.shengbojia.notes

import androidx.lifecycle.LiveData
import org.junit.Assert

fun assertLiveDataEventTriggered(
    liveData: LiveData<Event<String>>,
    taskId: String
) {
    val value = LiveDataTestUtil.getValue(liveData)
    Assert.assertEquals(value.getContentIfNotUsed(), taskId)
}

fun assertSnackbarMessage(snackbarLiveData: LiveData<Event<Int>>, messageId: Int) {
    val value: Event<Int> = LiveDataTestUtil.getValue(snackbarLiveData)
    Assert.assertEquals(value.getContentIfNotUsed(), messageId)
}