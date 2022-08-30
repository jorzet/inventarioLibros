package com.app.inventario.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.inventario.databinding.FragmentLoginBinding
import com.app.inventario.interactor.UserInteractor
import com.app.inventario.model.Error
import com.app.inventario.model.LoginAction
import com.app.inventario.model.User
import com.app.inventario.ui.HomeActivity
import com.app.inventario.ui.LoginActivity
import com.app.inventario.utils.click
import com.app.inventario.utils.onKeyEventListener
import com.app.inventario.viewmodel.LoginViewModel

class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = LoginViewModel()

        setUpListener()
        return binding.root
    }

    private fun setUpListener() {
        binding.closeApp click {
            activity?.finish()
        }
        binding.loginButton click {
            login()
        }
        binding.registerButton click {
            showRegister()
        }
        binding.clickHereTextView click {
            sendEmail()
        }
        binding.passwordTextInputEditText onKeyEventListener { view, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.loginButton.performClick()
                true
            } else false
        }
    }

    private fun login() {
        val nick = binding.userNickTextInputEditText.text.toString()
        val pass = binding.passwordTextInputEditText.text.toString()
        if (nick.isNotEmpty()) {
            if (pass.isNotEmpty()) {
                viewModel.getUser(nick, pass, object: UserInteractor.OnGetUserListener {
                    override fun onGetUserSuccess(user: User?) {
                        if (user != null ) {
                            activity?.let {
                                viewModel.saveSession(it.baseContext, user)
                            }
                            showHome()
                        } else {
                            showError("Necesita ingresar una contraseña")

                        }
                    }

                    override fun onGetUserError(error: Error) {
                        showError(error.error.toString())
                    }
                })
            } else {
                showError("Necesita ingresar una contraseña")
            }
        } else {
            showError("Necesita ingrear un email")
        }
    }

    private fun showHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra(LoginActivity.LOGIN_ACTION, LoginAction.LOGIN_ACTION)
        activity?.let{
            it.finish()
            startActivity(intent)
        }
    }

    private fun showRegister() {
        activity?.let {
            (it as LoginActivity).showRegisterFragment()
        }
    }

    private fun sendEmail() {
        if (binding.userNickTextInputEditText.text.toString().isEmpty()) {
            showError("Ingrese su nick para poder reestablecer su contraseña")
        } else {
            val emailIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "ralphmagnifico@gmail.com", null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password reset")
            emailIntent.putExtra(Intent.EXTRA_TEXT, getMessage())
            startActivity(Intent.createChooser(emailIntent, "Elija una app para mandar un email..."))
        }
    }

    private fun getMessage(): String {
        return "Nick account to reset: " + binding.userNickTextInputEditText.text.toString()
    }

    private fun showError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
        }
    }
}