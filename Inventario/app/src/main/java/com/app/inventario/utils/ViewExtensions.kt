package com.app.inventario.utils

import android.view.KeyEvent
import android.view.View
import android.widget.EditText

infix fun View.click(click: () -> Unit) {
    setOnClickListener { click() }
}

infix fun EditText.onKeyEventListener(onKeyListener: (view: View, keyCode: Int, keyEvent: KeyEvent) -> Boolean) {
    setOnKeyListener { view, keyCode, keyEvent -> onKeyListener(view, keyCode, keyEvent) }
}