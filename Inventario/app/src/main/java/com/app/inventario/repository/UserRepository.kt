package com.app.inventario.repository

import com.app.inventario.model.User
import com.app.inventario.repository.connector.BDconnector
import java.sql.CallableStatement
import java.sql.SQLException
import java.sql.Types

class UserRepository {

    private lateinit var bdConnector: BDconnector

    fun saveUser(user: User): String? {
        bdConnector = BDconnector()
        var sp: CallableStatement? = null
        var result = ""

        try {
            if (bdConnector.startConnection() != null) {
                val SQL = "{call insertUser (?,?,?,?,?)}"
                sp = bdConnector.conexionMySQL.prepareCall(SQL)
                sp.setEscapeProcessing(true)
                sp.queryTimeout = 20
                sp.setString(1, user.email)
                sp.setString(2, user.password)
                sp.setString(3, user.nick)
                sp.setString(4, user.completeName)
                sp.registerOutParameter(5, Types.VARCHAR)
                sp.execute()
                result = sp.getString(5)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                bdConnector.closeConnection()
                sp?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun getUser(nick: String, password: String): User? {
        bdConnector = BDconnector()
        var sp: CallableStatement? = null
        lateinit var user: User
        var result = ""
        var f = 0

        try {
            if (bdConnector.startConnection() != null) {
                val SQL = "{call getUser (?,?,?)}"
                sp = bdConnector.conexionMySQL.prepareCall(SQL)
                sp.setEscapeProcessing(true)
                sp.queryTimeout = 20
                sp.setString(1, nick)
                sp.setString(2, password)
                sp.registerOutParameter(3, Types.VARCHAR)
                val rs = sp.executeQuery()
                result = sp.getString(3)

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
                            user = User(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5)
                            )
                        }
                        return user
                    }
                } else return null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                bdConnector.closeConnection()
                sp?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return user
    }
}