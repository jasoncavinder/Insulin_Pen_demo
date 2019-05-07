//package com.jasoncavinder.inpen.demo
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProviders
//import androidx.navigation.NavController
//import androidx.navigation.findNavController
//import com.jasoncavinder.inpen.demo.data.LoginViewModel
//
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var navController: NavController
//
//    private lateinit var _loginViewModel: LoginViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        navController = findNavController(R.id.nav_host_login)
//
//        _loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
//
//    /*
//     TODO: setup biometrics
//    val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
//        .setTitle("TODO: Title")
//        .setSubtitle("TODO: Subtitle")
//        .setDescription("TODO: Description")
//        .setNegativeButtonText("TODO: Cancel")
//        .build()
//    val biometricPrompt = BiometricPrompt(activity, Executor {  }, BiometricPrompt.AuthenticationCallback())
//    */
//
//    }
//
///*
//]
//        userViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                INVALID_AUTHENTICATION -> {
//                    Snackbar.make(
//                        view,
//                        userViewModel.getAuthenticationFailedReason().value ?: "Unknown Error",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//                else -> {
//                }
//            }
//        })
//*/
//
//    /*
//    private fun hideKeyboard() {
//        val view = activity.view
//        view.let {
//            val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(v.windowToken, 0)
//        }
//    }
//    */
//
//}
//
//
////        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
////            val loginState = it ?: return@Observer
////
////            // disable ui button unless both username / password is valid
////            ui.isEnabled = loginState.isDataValid
////
////            if (loginState.emailError != null) {
////                username.error = getString(loginState.emailError)
////            }
////            if (loginState.passwordError != null) {
////                password.error = getString(loginState.passwordError)
////            }
////        })
////
////        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
////            val loginResult = it ?: return@Observer
////
////            loading.visibility = View.GONE
////            if (loginResult.error != null) {
////                showLoginFailed(loginResult.error)
////            }
////            if (loginResult.success != null) {
////                updateUiWithUser(loginResult.success)
////            }
////            setResult(AppCompatActivity.RESULT_OK)
////
////            //Complete and destroy ui activity once successful
////            finish()
////        })
////
////        username.afterTextChanged {
////            loginViewModel.loginDataChanged(
////                username.text.toString(),
////                password.text.toString()
////            )
////        }
////
////        password.apply {
////            afterTextChanged {
////                loginViewModel.loginDataChanged(
////                    username.text.toString(),
////                    password.text.toString()
////                )
////            }
////
////            setOnEditorActionListener { _, actionId, _ ->
////                when (actionId) {
////                    EditorInfo.IME_ACTION_DONE ->
////                        loginViewModel.ui(
////                            username.text.toString(),
////                            password.text.toString()
////                        )
////                }
////                false
////            }
////
////            ui.setOnClickListener {
////                loading.visibility = View.VISIBLE
////                loginViewModel.ui(username.text.toString(), password.text.toString())
////            }
////        }
////    }
////
////    private fun updateUiWithUser(model: LoggedInUserView) {
////        val welcome = getString(R.string.welcome)
////        val firstName = model.firstName
////        // TODO : initiate successful logged in experience
////        Toast.makeText(
////            applicationContext,
////            "$welcome $firstName",
////            Toast.LENGTH_LONG
////        ).show()
////    }
////
////    private fun showLoginFailed(@StringRes errorString: Int) {
////        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
////    }
////}
////
/////**
//// * Extension function to simplify setting an afterTextChanged action to EditText components.
//// */
////fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
////    this.addTextChangedListener(object : TextWatcher {
////        override fun afterTextChanged(editable: Editable?) {
////            afterTextChanged.invoke(editable.toString())
////        }
////
////        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
////
////        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
////    })
////}
//
///**
// * Extension function to simplify setting an afterTextChanged action to EditText components.
// */
//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}
