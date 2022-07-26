package com.app.inventario.interactor

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.app.inventario.model.Sell
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


class SaveSellLocalInteractor(context: Context) {
    private val mContext: Context = context
    val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    companion object {
        private const val SELLS_KEY = "SELLS_KEY"
        private const val COUNT_LOCAL = "COUNT_LOCAL"
    }

    fun execute(sell: Sell, countLocal: Int) {
        val editor = preferences.edit()
        val sellsJson = preferences.getString(SELLS_KEY, "")
        val sellList = arrayListOf<Sell>()

        if (!sellsJson.equals("", ignoreCase = true)) {
            val jsonArray = JSONArray(sellsJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.get(i) as JSONObject
                val sellObject = Gson().fromJson(jsonObject.toString(), Sell::class.java)
                sellList.add(sellObject)
            }
        }

        sellList.add(sell)
        val json: String = Gson().toJson(sellList)
        editor.putString(SELLS_KEY, json)
        editor.putInt(COUNT_LOCAL, countLocal)
        editor.apply()
    }

    fun getSavedSells(): List<Sell>? {
        val sellsJson = preferences.getString(SELLS_KEY, "")
        val sellList = arrayListOf<Sell>()
        if (!sellsJson.equals("", ignoreCase = true)) {
            val jsonArray = JSONArray(sellsJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.get(i) as JSONObject
                val sell = Gson().fromJson(jsonObject.toString(), Sell::class.java)
                sellList.add(sell)
            }
            return sellList
        } else return null
    }

    fun getCountLocal(): Int {
        return preferences.getInt(COUNT_LOCAL, -1)
    }
    fun deteleAllLocal() {
        preferences.edit().clear().commit();
    }
}