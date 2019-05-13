/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.jasoncavinder.insulinpendemoapp.MainActivity
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login_login.*


class LoginFragment : Fragment() {

    private lateinit var _loginViewModel: LoginViewModel
    private lateinit var _navController: NavController

    /*
    // TODO: setup biometrics
    val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("TODO: Title")
        .setSubtitle("TODO: Subtitle")
        .setDescription("TODO: Description")
        .setNegativeButtonText("TODO: Cancel")
        .build()
//    val biometricPrompt = BiometricPrompt(activity, Executor {  }, BiometricPrompt.AuthenticationCallback())
*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _navController = findNavController()

        _loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        _loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            button_do_login.isEnabled = loginState.isDataValid

            loginState.emailError?.let {
                edit_text_email.error = getString(loginState.emailError)
            }
            loginState.passwordError?.let {
                edit_text_pass.error = getString(loginState.passwordError)
            }
        })

        _loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            when (loginResult) {
                is Result.Error -> {
                    showLoginFailed(loginResult.exception.localizedMessage)
                    loading_bar.visibility = View.GONE
                    group_form_login.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    requireActivity().apply {
                        this.setResult(
                            Activity.RESULT_OK,
                            Intent(activity, MainActivity::class.java)
                        )
                    }.finish()
                }
            }
        })

        fun dataChanged() = _loginViewModel.loginDataChanged(
            edit_text_email.text.toString(),
            edit_text_pass.text.toString()
        )

        edit_text_email.afterTextChanged { dataChanged() }
        edit_text_pass.apply { afterTextChanged { dataChanged() } }

        button_cancel_login.setOnClickListener {
            hideKeyboard()
            _navController.navigateUp()
        }

        button_do_login.setOnClickListener {
            loading_bar.visibility = View.VISIBLE
            group_form_login.visibility = View.GONE
            _loginViewModel.login(
                edit_text_email.text.toString(),
                edit_text_pass.text.toString()
            )
            hideKeyboard()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            object : OnBackPressedCallback(
                true // default to enabled
            ) {
                override fun handleOnBackPressed() {
                    cancel()
                }
            }
        )
    }

    private fun showLoginFailed(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
    }

    private fun cancel() {
        hideKeyboard()
        _navController.navigateUp()
    }

    private fun hideKeyboard() {
        val view = this.view
        view?.let { v ->
            val imm = context?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
