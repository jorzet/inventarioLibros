package com.app.inventario.repository.connector

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class BDconnector {

    companion object {
        private const val TAG = "BDConnection"
        private const val driver = "com.mysql.jdbc.Driver"
        private const val ip = "192.168.254.182"
        private const val port = "3306"
        private const val dataBase = "inventarioLibros"
        private const val user = "jorge"
        private const val password = "jorge"
    }
    public lateinit var conexionMySQL: Connection

    fun startConnection(): Connection? {
        //Construímos la url para establecer la conexión
        try {
            Class.forName(driver).newInstance();
            val url =
                "jdbc:mysql://$ip:$port/$dataBase?user=$user&password=$password&?useSSL=false&useJDBCComplaintTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            conexionMySQL = DriverManager.getConnection(url)
            //Comprobamos que la conexión se ha establecido
            if (!conexionMySQL.isClosed()) {
                Log.d(TAG, "Conexion establecida:")
            }
        } catch (ex: Exception) {
            Log.d(TAG, "Error al comprobar las credenciales:" + ex.message)
            ex.printStackTrace()
            return null
        }

        return conexionMySQL
    }

    fun closeConnection() {
        try {
            conexionMySQL.close()
            Log.d(TAG, "Conexion cerrada:")
        } catch (e: SQLException) {
            Log.d(TAG, "Error al cerrar conexion:" + e.printStackTrace())
        }
    }
}