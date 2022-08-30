package com.app.inventario.viewmodel

import android.content.Context
import com.app.inventario.interactor.SaveSessionLocalInteractor
import com.app.inventario.interactor.UserInteractor
import com.app.inventario.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel: BaseViewModel() {

    fun getUser(nick: String, password: String, onGetUserListener: UserInteractor.OnGetUserListener) {
        GlobalScope.launch {
            UserInteractor().executeGetClient(nick, password, onGetUserListener)
        }
    }
}