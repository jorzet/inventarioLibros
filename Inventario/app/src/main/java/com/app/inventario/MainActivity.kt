package com.app.inventario

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.inventario.databinding.ActivityMainBinding
import com.app.inventario.interactor.SaveSellInteractor
import com.app.inventario.interactor.SaveSellLocalInteractor
import com.app.inventario.model.Sell
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mNetworkChangeReceiver: NetworkChangeReceiver

    private var countRegisters = 0
    private var isConnected: Boolean = true
    private var savePending: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        binding.registerCountTextview.text = SaveSellLocalInteractor(baseContext).getCountLocal().toString()
        registerNetworkBroadcastForNougat()
    }

    private fun setListeners() {
        binding.sendCloudButton.setOnClickListener {
            handleSaveBook()
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        mNetworkChangeReceiver = NetworkChangeReceiver(object : ConnectionNetworkListener {
            override fun onConnected() {
                showError("Conectado")
                isConnected = true

                if (savePending) {
                    val sells = SaveSellLocalInteractor(baseContext).getSavedSells()
                    sells?.forEach {
                        SaveSellInteractor().execute(it)

                    }
                    SaveSellLocalInteractor(baseContext).deteleAllLocal()
                    binding.registerCountTextview.text = "0"
                    countRegisters = 0
                    savePending = false
                }
            }
            override fun onDisconnected() {
                showError("Desconectado")
                isConnected = false
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun handleSaveBook() {
        val productId = binding.productEdittext.text.toString()
        val clientId = binding.clientEdittext.text.toString()
        if (productId.isNotEmpty()) {
            if (clientId.isNotEmpty()) {
                binding.productEdittext.setText("")
                binding.clientEdittext.setText("")
                countRegisters++
                binding.registerCountTextview.text = countRegisters.toString()
                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateandTime: String = sdf.format(Date())

                val sell = Sell(productId, clientId, currentDateandTime, "status")

                if (isConnected) {
                    SaveSellInteractor().execute(sell)
                } else {
                    SaveSellLocalInteractor(this).execute(sell, countRegisters)
                    savePending = true
                }
            } else {
                showError("Por favor introduce el id del cliente")
            }
        } else {
            showError("Por favor introduce el id del producto")
        }
    }

    fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT)
    }

    interface ConnectionNetworkListener {
        fun onConnected()
        fun onDisconnected()
    }

    class NetworkChangeReceiver(connectionNetworkListener: ConnectionNetworkListener): BroadcastReceiver() {
        val mConnectionNetworkListener = connectionNetworkListener
        override fun onReceive(context: Context?, p1: Intent?) {
            try {
                val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                val isConnected = netInfo != null && netInfo.isConnected
                if (isConnected) {
                    mConnectionNetworkListener.onConnected()
                } else {
                    mConnectionNetworkListener.onDisconnected()
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()

            }
        }

    }
}