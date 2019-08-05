package com.shengbojia.notes

import android.app.Application
import com.shengbojia.notes.data.Repository

class NotesApplication : Application() {

    // Depends on flavour
    val repository: Repository
        get() = ServiceLocator.provideRepository(this)

}