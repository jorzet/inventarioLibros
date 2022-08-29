package com.app.inventario.viewmodel

import com.app.inventario.interactor.UserInteractor
import com.app.inventario.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterViewModel: BaseViewModel() {

    fun saveUser(user: User, onSaveUserListener: UserInteractor.OnSaveUserListener) {
        GlobalScope.launch {
            UserInteractor().executeSaveClient(user, onSaveUserListener)
        }
    }
}