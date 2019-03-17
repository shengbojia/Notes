package com.shengbojia.notes.worker

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shengbojia.notes.data.AppDatabase
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PopulateDatabaseWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override val coroutineContext = Dispatchers.IO

    @WorkerThread
    override suspend fun doWork(): Result = withContext(coroutineContext) {

            try {
                val database = AppDatabase.getInstance(applicationContext)
                database.apply {
                    noteDao().insert(
                        Note(
                            title = "Capture what's on your mind, on the go",
                            description = "description 1",
                            priority = 1
                        )
                    )
                    noteDao().insert(
                        Note(
                            title = "Test title 2",
                            description = "description 2",
                            priority = 2
                        )
                    )
                }

                Result.success()

            } catch (e: Exception) {

                Log.e(TAG, "Error trying to prepopulate database")
                Result.failure()

            }
        }

    companion object {
        private const val TAG = "WorkerPopulate"
    }
}