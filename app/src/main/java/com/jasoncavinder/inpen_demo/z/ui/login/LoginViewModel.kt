//package com.jasoncavinder.inpen_demo.z.ui.ui
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import android.util.Patterns
//
//import com.jasoncavinder.inpen_demo.R
//import com.jasoncavinder.inpen.demo.data.entities.user.UserRepository
//
//class LoginViewModel internal constructor(private val userRepository: UserRepository) : ViewModel() {
//
//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm
//
//    private val _loginResult = MutableLiveData<LoginResult>()
//    val loginResult: LiveData<LoginResult> = _loginResult
//
//    fun ui(username: String, password: String) {
//        // can be launched in a separate asynchronous job
//        val result = userRepository.ui(username, password)
//
//        if (result is Result.Success) {
//            _loginResult.value = LoginResult(success = LoggedInUserView(result.data.userID, result.data.firstName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
//    }
//
//    fun loginDataChanged(username: String, password: String) {
//        if (!isUserNameValid(username)) {
//            _loginForm.value = LoginFormState(emailError = R.string.invalid_username)
//        } else if (!isPasswordValid(password)) {
//            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
//        } else {
//            _loginForm.value = LoginFormState(isDataValid = true)
//        }
//    }
//
//    // A placeholder username validation check
//    private fun isUserNameValid(username: String): Boolean {
//        return if (username.contains('@')) {
//            Patterns.EMAIL_ADDRESS.matcher(username).matches()
//        } else {
//            username.isNotBlank()
//        }
//    }
//
//    // A placeholder password validation check
//    private fun isPasswordValid(password: String): Boolean {
//        return password.length > 5;
//    }
//
//    companion object {
//
//    }
//
//}
