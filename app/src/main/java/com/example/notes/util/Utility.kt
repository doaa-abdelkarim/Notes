package com.example.notes.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.Calendar


fun formatDate(calendar: Calendar) = SimpleDateFormat("MMM dd, YYYY")
    .format(calendar.time)

fun closeKeyboard (activity: Activity){
    val view = activity.currentFocus
    view?.let {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
