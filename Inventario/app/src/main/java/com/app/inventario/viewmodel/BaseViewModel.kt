package com.app.inventario.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.app.inventario.interactor.SaveSessionLocalInteractor
import com.app.inventario.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

abstract class BaseViewModel {
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    open fun saveSession(context: Context, user: User) {
        GlobalScope.launch {
            SaveSessionLocalInteractor(context).execute(user)
        }
    }

    open fun validateEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}