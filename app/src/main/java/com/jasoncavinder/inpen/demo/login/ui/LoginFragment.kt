package com.jasoncavinder.inpen.demo.login.ui

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
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.jasoncavinder.inpen.demo.MainActivity
import com.jasoncavinder.inpen.demo.R
import com.jasoncavinder.inpen.demo.data.LoginViewModel
import com.jasoncavinder.inpen.demo.utilities.afterTextChanged
import kotlinx.android.synthetic.main.fragment_login_login.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

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

            group_form_login.visibility = View.VISIBLE
            loading_bar.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                _loginViewModel.user.value?.userId?.let {
                    activity?.setResult(
                        Activity.RESULT_OK,
                        Intent(activity, MainActivity::class.java)
                            .putExtra("userID", it)
                    )
                    activity?.finish()
                }
            }
        })

        fun dataChanged() = _loginViewModel.loginDataChanged(
            edit_text_email.text.toString(),
            edit_text_pass.text.toString()
        )

        edit_text_email.afterTextChanged { dataChanged() }
        edit_text_pass.apply { afterTextChanged { dataChanged() } }

/*
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        _loginViewModel.login(
                            edit_text_email.text.toString(),
                            edit_text_pass.text.toString()
                        )
                }
                false
            }
        }
*/

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.intent?.getStringExtra("userID")?.let { edit_text_email.setText(it) }
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

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(this.context, errorString, Toast.LENGTH_LONG).show()
    }

    private fun cancel() {
        hideKeyboard()
//        _loginViewModel.userCancelledLogin()
        _navController.navigateUp()
    }

    private fun hideKeyboard() {
        val view = this.view
        view?.let { v ->
            val imm = context?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
