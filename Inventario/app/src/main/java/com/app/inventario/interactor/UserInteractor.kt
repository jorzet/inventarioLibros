package com.app.inventario.interactor

import com.app.inventario.model.Error
import com.app.inventario.model.User
import com.app.inventario.repository.UserRepository


class UserInteractor {

    interface OnGetUserListener {
        fun onGetUserSuccess(user: User?)
        fun onGetUserError(error: Error)
    }

    interface OnSaveUserListener {
        fun onSaveUserSuccess()
        fun onSaveUserError(error: Error)
    }

    private val repository: UserRepository = UserRepository()

    suspend fun executeSaveClient(user: User, onSaveUserListener: OnSaveUserListener) {
        try {
            val response = repository.saveUser(user)
            if (response.equals("OK")) {
                onSaveUserListener.onSaveUserSuccess()
            } else {
                val error = Error(response, 0)
                onSaveUserListener.onSaveUserError(error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val error = Error("Ocurrio un problema, vuelve a intentarlo", 0)
            onSaveUserListener.onSaveUserError(error)
        }
    }

    suspend fun executeGetClient(nick: String, password: String, onGetUserListener: OnGetUserListener) {
        try {
            val user = repository.getUser(nick, password)
            if (user != null) {
                onGetUserListener.onGetUserSuccess(user)
            } else {
                val error = Error("Ocurrio un problema, vuelve a intentarlo", 0)
                onGetUserListener.onGetUserError(error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val error = Error("Ocurrio un problema, vuelve a intentarlo", 0)
            onGetUserListener.onGetUserError(error)
        }
    }
}