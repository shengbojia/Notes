package com.shengbojia.notes.data.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.worker.PopulateDatabaseWorker

/**
 * Room database for the app.
 */
@Database(entities = [Note::class], version = 1 )
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    // Singleton instantiation.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context): AppDatabase =
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                )
                    .addCallback(RoomCallBack)
                    .build()
    }

    // Pre-populate the database with some helpful notes
    private object RoomCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
            WorkManager.getInstance().enqueue(request)
        }
    }

    // Added a new column, performed migration since I did not want to clear out the app's data on my phone
    private object Migration1To2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE note_table ADD COLUMN date_written INTEGER")
        }
    }

    // Worker used instead
    /*
    private class PopulateDbAsyncClass constructor(db: AppDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private var noteDao = db?.noteDao() ?: throw IllegalStateException("Database uninitialized")

        override fun doInBackground(vararg params: Unit?) {
            noteDao.insert(
                Note(
                    title = "Capture what's on your mind, on the go",
                    description = "description 1",
                    priority = 1
                )
            )
            noteDao.insert(
                Note(
                    title = "Test title 2",
                    description = "description 2",
                    priority = 2
                )
            )
        }
    }
    */
}