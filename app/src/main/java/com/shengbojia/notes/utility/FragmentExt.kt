package com.shengbojia.notes.utility

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.shengbojia.notes.viewmodel.ViewModelFactory

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {

    val repository = InjectorUtils.getNoteRepository(this.requireContext())
    return ViewModelProviders.of(this, ViewModelFactory(repository)).get(viewModelClass)
}