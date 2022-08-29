package com.app.inventario.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.inventario.databinding.ActivityLoginBinding
import com.app.inventario.ui.fragments.LoginFragment
import com.app.inventario.ui.fragments.RegisterFragment

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        const val LOGIN_ACTION = "login_action"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerView.id, LoginFragment())
            .addToBackStack("login_fragment")
            .commit()
    }

    fun showRegisterFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerView.id, RegisterFragment())
            .addToBackStack("register_fragment")
            .commit()
    }
}