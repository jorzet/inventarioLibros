package com.app.inventario.connector

import com.app.inventario.model.Sell
import java.lang.Exception
import java.sql.CallableStatement
import java.sql.SQLException
import java.sql.Types
import java.text.SimpleDateFormat
import java.util.*

class Repository {
    private lateinit var bdConnector: BDconnector

    fun saveBook(sell: Sell): String{

        bdConnector = BDconnector()
        var sp: CallableStatement? = null
        var result = ""

        try {
            if (bdConnector.startConnection() != null) {
                val SQL = "{call insertBook (?,?,?,?,?)}"
                sp = bdConnector.conexionMySQL.prepareCall(SQL)
                sp.setEscapeProcessing(true)
                sp.queryTimeout = 20
                sp.setString(1, sell.productId)
                sp.setString(2, sell.clientId)
                sp.setString(3, sell.status)
                sp.setString(4, sell.date)
                sp.registerOutParameter(5, Types.VARCHAR)
                sp.execute()
                result = sp.getString(5)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                bdConnector.closeConnection()
                if (sp != null) sp.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }
}