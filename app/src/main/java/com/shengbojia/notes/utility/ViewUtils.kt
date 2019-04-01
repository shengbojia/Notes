package com.shengbojia.notes.utility

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Static methods for manipulating views.
 */
object ViewUtils {

    fun hideSoftKeyboard(activity: Activity?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}