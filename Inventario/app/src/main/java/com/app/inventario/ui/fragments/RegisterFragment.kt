package com.app.inventario.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.inventario.databinding.FragmentRegisterBinding
import com.app.inventario.interactor.UserInteractor
import com.app.inventario.model.Error
import com.app.inventario.model.LoginAction
import com.app.inventario.model.User
import com.app.inventario.ui.HomeActivity
import com.app.inventario.ui.LoginActivity
import com.app.inventario.utils.click
import com.app.inventario.utils.onKeyEventListener
import com.app.inventario.viewmodel.RegisterViewModel

class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        viewModel = RegisterViewModel()
        setUpListeners()
        return binding.root
    }

    private fun setUpListeners() {
        binding.registerButton click {
            register()
        }
        binding.cancelButton click {
            goBack()
        }
        binding.backImageView click {
            goBack()
        }
        binding.closeApp click {
            activity?.finish()
        }
        binding.completeNameTextInputEditText onKeyEventListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.registerButton.performClick()
                true
            } else false
        }
    }

    private fun register() {
        val email = binding.userTextInputEditText.text.toString()
        val pass = binding.passwordTextInputEditText.text.toString()
        val nick = binding.nickTextInputEditText.text.toString()
        val completeName = binding.completeNameTextInputEditText.text.toString()
        if (email.isNotEmpty()) {
            if (viewModel.validateEmail(email)) {
                if (pass.isNotEmpty()) {
                    if (nick.isNotEmpty()) {
                        if (completeName.isNotEmpty()) {
                            val user = User(0, email, pass, nick, completeName)
                            viewModel.saveUser(user, object : UserInteractor.OnSaveUserListener {
                                override fun onSaveUserSuccess() {
                                    activity?.let {
                                        viewModel.saveSession(it.baseContext, user)
                                    }
                                    showHome()
                                }

                                override fun onSaveUserError(error: Error) {
                                    showError(error.error.toString())
                                }
                            })
                        } else {
                            showError("Necesita ingresar su nombre completo")
                        }
                    } else {
                        showError("Necesita ingresar un nick")
                    }
                } else {
                    showError("Necesita ingresar una contraseña")
                }
            } else {
                showError("Ingrese un email valido ejemplo@ejemplo.com")
            }
        } else {
            showError("Necesita ingresar un email")
        }
    }

    private fun showHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra(LoginActivity.LOGIN_ACTION, LoginAction.REGISTER_ACTION)
        activity?.let {
            it.finish()
            it.startActivity(intent)
        }
    }

    private fun goBack() {
        activity?.onBackPressed()
    }

    private fun showError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }
}