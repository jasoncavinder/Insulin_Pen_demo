//package com.jasoncavinder.inpen_demo.ui.ui
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import android.util.Patterns
//import com.jasoncavinder.inpen_demo.data.LoginRepository
//import com.jasoncavinder.inpen_demo.persistence.Result
//
//import com.jasoncavinder.inpen_demo.R
//import com.jasoncavinder.inpen.demo.data.entities.user.User
//
//class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
//
//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm
//
//    private val _loginResult = MutableLiveData<LoginResult>()
//    val loginResult: LiveData<LoginResult> = _loginResult
//
//    fun ui(email: String, password: String) {
//        val result = loginRepository.ui(email, password)
//
//        if (result is Result.Success) {
//            @Suppress("UNCHECKED_CAST")
//            val user: LiveData<User> = result.data as LiveData<User>
//            _loginResult.value = LoginResult(
//                success = user.value?.toLoggedInUserView()
//            )
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
//    fun createUserAndLogin(email: String, firstName: String, lastName: String, password: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    fun userCancelledRegistration() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
