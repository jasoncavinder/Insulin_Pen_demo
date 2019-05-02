package com.jasoncavinder.inpen.demo.data

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jasoncavinder.inpen.demo.R
import com.jasoncavinder.inpen.demo.login.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var _parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val _coroutineContext: CoroutineContext
        get() = _parentJob + Dispatchers.Main

    private val scope = CoroutineScope(_coroutineContext)

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _createUserForm = MutableLiveData<CreateUserFormState>()
    val createUserFormState: LiveData<CreateUserFormState> = _createUserForm

    private val _createUserResult = MutableLiveData<CreateUserResult>()
    val createUserResult: LiveData<CreateUserResult> = _createUserResult

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
        val usersDao = AppDatabase.getInstance(application, scope).userDao()
        _loginRepository = LoginRepository.getInstance(usersDao)

        localUsers = _loginRepository.localUsers()
        _user.value = _loginRepository.user
    }

    fun login(email: String, password: String) {
        GlobalScope.launch {
            val result = _loginRepository.login(email, password)

            if (result is Result.Success) {
                _loginResult.postValue(LoginResult(success = result.data.asView()))
            } else {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    fun logout() = _loginRepository.logout()

    fun createUser(firstName: String, lastName: String, email: String, password: String, confirm: String) {
        GlobalScope.launch {
            val result = _loginRepository.createUser(firstName, lastName, email, password)

            when (result) {
                is Result.Success -> _createUserResult.postValue(CreateUserResult(success = result.data.asView()))
                else -> _createUserResult.postValue(CreateUserResult(error = R.string.create_user_failed))
            }
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun createUserDataChanged(firstName: String, lastName: String, email: String, password: String, confirm: String) {
        when {
            !isNameValid(firstName) ->
                _createUserForm.value = CreateUserFormState(firstNameError = R.string.invalid_first_name)
            !isNameValid(lastName) ->
                _createUserForm.value = CreateUserFormState(lastNameError = R.string.invalid_last_name)
            !isEmailValid(email) ->
                _createUserForm.value = CreateUserFormState(emailError = R.string.invalid_email)
            !isPasswordValid(password) ->
                _createUserForm.value = CreateUserFormState(passwordError = R.string.invalid_password)
            !isConfirmValid(password, confirm) ->
                _createUserForm.value = CreateUserFormState(confirmError = R.string.invalid_confirm)
            else ->
                _createUserForm.value = CreateUserFormState(isDataValid = true)
        }
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
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

