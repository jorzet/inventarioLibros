package com.app.inventario.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app.inventario.BuildConfig
import com.app.inventario.R
import com.app.inventario.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity() {

    companion object {
        private const val TIMEOUT: Long = 2000
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.versionTextView.text = getString(R.string.app_name) + " Ver. " + BuildConfig.VERSION_NAME

        Handler(Looper.getMainLooper()).postDelayed({
            showLoginActivity()
        },
            TIMEOUT
        )
    }

    private fun showLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.finish()
        startActivity(intent)
    }
}