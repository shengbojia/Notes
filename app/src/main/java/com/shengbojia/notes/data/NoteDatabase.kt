package com.shengbojia.notes.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask
import java.lang.IllegalStateException

@Database(entities = [Note::class], version = 1 )
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

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
                ).addCallback(RoomCallBack).build()
    }

    private object RoomCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Null safe as call back is only added after instantiation of INSTANCE
            PopulateDbAsyncClass(INSTANCE).execute()
        }
    }

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