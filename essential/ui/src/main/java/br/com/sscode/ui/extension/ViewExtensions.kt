package br.com.sscode.ui.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackMessage(message: String, duration: Int) {
    Snackbar.make(
        this,
        message,
        duration
    ).show()
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}