package com.shengbojia.notes.worker

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shengbojia.notes.data.db.AppDatabase
import com.shengbojia.notes.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

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
                            description = "description 1"
                        )
                    )
                    noteDao().insert(
                        Note(
                            title = "Test title 2",
                            description = "description 2"
                        )
                    )
                }

                Result.success()

            } catch (e: Exception) {

                Timber.e("Error trying to prepopulate database")
                Result.failure()

            }
        }
}