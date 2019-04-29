package com.jasoncavinder.inpen.demo.data

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.login.LoggedInUser
import com.jasoncavinder.inpen.demo.login.LoginFormState
import com.jasoncavinder.inpen.demo.login.LoginResult
import com.jasoncavinder.inpen.demo.login.Result
import com.jasoncavinder.inpen_demo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var _parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val _coroutineContext: CoroutineContext
        get() = _parentJob + Dispatchers.Main
    private val scope = CoroutineScope(_coroutineContext)


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _user = MutableLiveData<LoggedInUser>()
    val user: LiveData<LoggedInUser> = _user

    private val _loginRepository: LoginRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val localUsers: LiveData<Int>


    init {
        val wordsDao = AppDatabase.getInstance(application).userDao()
        _loginRepository = LoginRepository.getInstance(wordsDao)
        localUsers = _loginRepository.localUsers()
        _user.value = _loginRepository.user
    }

    fun login(email: String, password: String) {

        val result = _loginRepository.login(email, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = result.data.asView())
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun logout() = _loginRepository.logout()


    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8;
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
//    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
//        repository.insert(word)
//    }
    fun isLoggedIn(): Boolean = _loginRepository.isLoggedIn

    override fun onCleared() {
        super.onCleared()
        _parentJob.cancel()
    }

}

