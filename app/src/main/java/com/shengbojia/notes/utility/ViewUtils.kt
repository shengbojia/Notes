package com.shengbojia.notes.utility

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Static methods for manipulating views.
 */
object ViewUtils {

    fun exitFocus(view: View?) {
        view?.requestFocus()
    }

    fun hideSoftKeyboard(activity: Activity?, view: View?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}