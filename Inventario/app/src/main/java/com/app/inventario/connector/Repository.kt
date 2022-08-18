package com.app.inventario.connector

import com.app.inventario.downloader.ClientDownloader
import com.app.inventario.model.Client
import com.app.inventario.model.Sell
import java.sql.CallableStatement
import java.sql.SQLException
import java.sql.Types
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

    fun saveClient(client: Client): String?{

        bdConnector = BDconnector()
        var sp: CallableStatement? = null
        var result = ""

        try {
            if (bdConnector.startConnection() != null) {
                val SQL = "{call insertClient (?,?,?,?)}"
                sp = bdConnector.conexionMySQL.prepareCall(SQL)
                sp.setEscapeProcessing(true)
                sp.queryTimeout = 20
                sp.setString(1, client.clientCode)
                sp.setString(2, client.clientName)
                sp.setString(3, client.status)
                sp.registerOutParameter(4, Types.VARCHAR)
                sp.execute()
                result = sp.getString(4)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
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

    fun getClient(clientCode: String): Client? {
        bdConnector = BDconnector()
        var sp: CallableStatement? = null
        lateinit var client: Client
        var result = ""
        var f = 0

        try {
            if (bdConnector.startConnection() != null) {
                val SQL = "{call getClient (?,?)}"
                sp = bdConnector.conexionMySQL.prepareCall(SQL)
                sp.setEscapeProcessing(true)
                sp.queryTimeout = 20
                sp.setString(1, clientCode)
                sp.registerOutParameter(2, Types.VARCHAR)
                val rs = sp.executeQuery()
                result = sp.getString(2)

                if (result.equals("OK")) {
                    while (rs.next()) {
                        f += 1
                    }
                    if (f == 0) {
                        System.out.println("Es nulo$rs")
                    } else {
                        rs.beforeFirst()
                        System.out.println("No es nulo$rs")
                        while (rs.next()) {
                            client = Client(rs.getString(1), rs.getString(2), rs.getString(3))
                        }
                        return client
                    }
                } else return null
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
        return client
    }
}