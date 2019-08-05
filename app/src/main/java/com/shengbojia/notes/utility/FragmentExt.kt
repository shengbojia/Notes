package com.shengbojia.notes.utility

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.shengbojia.notes.NotesApplication

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {

    val repository = (requireContext().applicationContext as NotesApplication).repository
    return ViewModelProviders.of(this, ViewModelFactory(repository)).get(viewModelClass)
}