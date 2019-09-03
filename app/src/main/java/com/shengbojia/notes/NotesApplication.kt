package com.shengbojia.notes

import android.app.Application
import com.shengbojia.notes.data.Repository
import timber.log.Timber

class NotesApplication : Application() {

    // Depends on flavour
    val repository: Repository
        get() = ServiceLocator.provideRepository(this)

    override fun onCreate() {
        super.onCreate()

        // Plant a new debug log tree for DEBUG build config
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}