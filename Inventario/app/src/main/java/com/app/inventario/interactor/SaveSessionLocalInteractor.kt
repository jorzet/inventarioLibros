package com.app.inventario.interactor

import android.content.Context
import android.preference.PreferenceManager
import com.app.inventario.model.Sell
import com.app.inventario.model.User
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class SaveSessionLocalInteractor(context: Context) {
    private val mContext: Context = context
    val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    companion object {
        private const val USER_KEY = "user_key"
    }

    fun execute(user: User) {
        val editor = preferences.edit()

        val json: String = Gson().toJson(user)
        editor.putString(USER_KEY, json)
        editor.apply()
    }

    fun getSavedSession(): User? {
        val userJson = preferences.getString(USER_KEY, "")
        val user = Gson().fromJson(userJson, User::class.java)
        return user
    }

    fun deleteSession() {
        preferences.edit().clear().commit();
    }
}