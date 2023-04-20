package br.com.sscode.ui.extension

import android.view.ViewStub

fun ViewStub.setVisibleOrInflate() {
    if (isInflated()) {
        setVisible()
    } else {
        inflate()
    }
}

private fun ViewStub.isInflated() = inflatedId != 0