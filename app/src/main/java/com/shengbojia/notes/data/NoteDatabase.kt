package com.shengbojia.notes.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.os.AsyncTask
import java.lang.IllegalStateException

/**
 * Room database for the app.
 */
@Database(entities = [Note::class], version = 2 )
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    // Singleton instantiation.
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context): NoteDatabase =
                Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .addCallback(RoomCallBack)
                    .addMigrations(Migration1To2)
                    .build()
    }

    // Pre-populate the database with some helpful notes
    private object RoomCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Null safe as call back is only added after instantiation of INSTANCE
            PopulateDbAsyncClass(INSTANCE).execute()
        }
    }

    // Added a new column, performed migration since I did not want to clear out the app's data on my phone
    private object Migration1To2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE note_table ADD COLUMN date_written INTEGER")
        }
    }

    // TODO: Use Kotlin's co-routines instead of AsyncTask
    private class PopulateDbAsyncClass constructor(db: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
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
}