package com.app.inventario.utils

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.app.inventario.R

infix fun View.click(click: () -> Unit) {
    setOnClickListener { click() }
}

infix fun EditText.onKeyEventListener(onKeyListener: (view: View, keyCode: Int, keyEvent: KeyEvent) -> Boolean) {
    setOnKeyListener { view, keyCode, keyEvent -> onKeyListener(view, keyCode, keyEvent) }
}

fun ImageView.showOnline(): ImageView {
    this.setImageResource(R.drawable.ic_green_dot)
    return this
}

fun ImageView.showOffline(): ImageView {
    this.setImageResource(R.drawable.ic_red_dot)
    return this
}