package com.app.inventario

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.inventario.databinding.ActivityMainBinding
import com.app.inventario.downloader.ClientDownloader
import com.app.inventario.downloader.showImage
import com.app.inventario.downloader.showName
import com.app.inventario.downloader.showPlaceHolder
import com.app.inventario.interactor.SaveSellInteractor
import com.app.inventario.interactor.SaveSellLocalInteractor
import com.app.inventario.model.Sell
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), ClientDownloader.OnShowImageListener{

    // binding
    private lateinit var binding: ActivityMainBinding

    // receiver
    private lateinit var mNetworkChangeReceiver: NetworkChangeReceiver

    // flags
    private var countRegisters = 0
    private var isConnected: Boolean = true
    private var savePending: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        binding.productEdittext.requestFocus()
        binding.registerCountTextview.text = SaveSellLocalInteractor(baseContext).getCountLocal().toString()
        registerNetworkBroadcastForNougat()
    }

    /**
     * Set up listeners
     */
    private fun setListeners() {
        binding.sendCloudButton.setOnClickListener {
            binding.productEdittext.requestFocus()
            handleSaveBook()
            cleanViews()
        }
        binding.addClientButton.setOnClickListener {
            showAddClient()
        }
        binding.clientEdittext.setOnKeyListener { view, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val client = binding.clientEdittext.text.toString().toUpperCase()
                binding.clientImageview.showImage(client, this)
                GlobalScope.launch {
                    binding.clientNameTextView.showName(client)
                }
                runOnUiThread {
                    binding.productEdittext.requestFocus()
                }
                handleSaveBook()
                cleanViews()
                true
            } else false
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

    private fun handleSaveBook() {
        val productId = binding.productEdittext.text.toString()
        val clientId = binding.clientEdittext.text.toString()
        if (productId.isNotEmpty()) {
            if (clientId.isNotEmpty()) {
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

    fun cleanViews() {
        // reset views
        binding.productEdittext.run {
            setText("")
            requestFocus()
        }
        binding.clientEdittext.setText("")
        binding.clientNameTextView.text = ""
        binding.clientImageview.showPlaceHolder()
    }

    private fun showAddClient() {

    }

    fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Image loading events
     */
    override fun onImageLoaded() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onImageError() {
        Toast.makeText(this, "Error imagen del cliente no encontrada", Toast.LENGTH_LONG).show()
    }
    /** end region **/


    /**
     * Connection Listener
     */
    interface ConnectionNetworkListener {
        fun onConnected()
        fun onDisconnected()
    }

    /**
     * Connection BroadcastReceiver
     */
    class NetworkChangeReceiver(): BroadcastReceiver() {
        private lateinit var mConnectionNetworkListener: ConnectionNetworkListener

        constructor(connectionNetworkListener: ConnectionNetworkListener): this() {
            mConnectionNetworkListener = connectionNetworkListener
        }

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