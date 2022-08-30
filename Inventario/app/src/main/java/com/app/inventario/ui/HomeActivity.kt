package com.app.inventario.ui

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
import com.app.inventario.databinding.ActivityHomeBinding
import com.app.inventario.downloader.ClientDownloader
import com.app.inventario.downloader.showImage
import com.app.inventario.downloader.showName
import com.app.inventario.downloader.showPlaceHolder
import com.app.inventario.interactor.SaveSellInteractor
import com.app.inventario.interactor.SaveSellLocalInteractor
import com.app.inventario.interactor.SaveSessionLocalInteractor
import com.app.inventario.model.LoginAction
import com.app.inventario.model.Sell
import com.app.inventario.utils.click
import com.app.inventario.utils.onKeyEventListener
import com.app.inventario.utils.showOffline
import com.app.inventario.utils.showOnline
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity(), ClientDownloader.OnShowImageListener{

    // binding
    private lateinit var binding: ActivityHomeBinding

    // receiver
    private lateinit var mNetworkChangeReceiver: NetworkChangeReceiver

    // flags
    private var countRegisters = 0
    private var isConnected: Boolean = true
    private var savePending: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        handleExtras()
        showWelcome()
        binding.productEdittext.requestFocus()
        binding.registerCountTextview.text = SaveSellLocalInteractor(baseContext).getCountLocal().toString()
        registerNetworkBroadcastForNougat()
    }

    private fun handleExtras() {
        val action = intent.getStringExtra(LoginActivity.LOGIN_ACTION)
        if (action != null && action == LoginAction.REGISTER_ACTION.toString()) {

        }
    }

    private fun showWelcome() {
        val user = SaveSessionLocalInteractor(baseContext).getSavedSession()
        val welcome = "Bienvenido   " + (user?.completeName ?: "")
        binding.welcomeTextView.text = welcome
    }

    /**
     * Set up listeners
     */
    private fun setListeners() {
        binding.sendCloudButton click  {
            binding.productEdittext.post {  binding.productEdittext.requestFocus() }
            handleSaveBook()
            cleanViews()
        }
        binding.addClientButton click  {
            showAddClient()
        }
        binding.closeApp click {
            finish()
        }
        binding.clientEdittext onKeyEventListener { view, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.sendCloudButton.performClick()
                true
            } else false
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        mNetworkChangeReceiver = NetworkChangeReceiver(object : ConnectionNetworkListener {
            override fun onConnected() {
                binding.onlineImageView.showOnline()
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
                binding.onlineImageView.showOffline()
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
        val client = binding.clientEdittext.text.toString().uppercase()
        binding.clientImageview.showImage(client, this)
        GlobalScope.launch {
            binding.clientNameTextView.showName(client)
        }

        val productId = binding.productEdittext.text.toString()
        val clientId = binding.clientEdittext.text.toString()
        if (productId.isNotEmpty()) {
            if (clientId.isNotEmpty()) {
                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())

                val sell = Sell(productId, clientId, currentDateAndTime, "status")

                if (isConnected) {
                    SaveSellInteractor().execute(sell)
                } else {
                    countRegisters++
                    binding.registerCountTextview.text = countRegisters.toString()
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

    private fun cleanViews() {
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