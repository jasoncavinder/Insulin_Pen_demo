/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.ui.login.LoginFormState
import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    /*
    Educational Note:

    This application's class is 'DemoApplication' which extends 'Application'.
    The only difference is to preload some photos from res to internal storage
    for demo purposes. The real version of this app would load those resources
    from an online resource.

    Here we are telling android to build the viewmodel using default android
    factory for Application and NOT DemoApplication. This is necessary because
    android of course does not supply a custom factory for our custom
    application class, and this works because our extension of Application
    doesn't modify anything that would affect viewmodels.

    So, although it may seem natural to put our actual 'DemoApplication' class
    here, DON'T!

    ~ Jason
     */
    application: Application /* !!! NOT DemoApplication !!! */
) : AndroidViewModel(application) {

    private val repository: AppRepository = AppRepository.getInstance(
        AppDatabase.getInstance(application).userDao(),
        AppDatabase.getInstance(application).providerDao(),
        AppDatabase.getInstance(application).penDao(),
        AppDatabase.getInstance(application).penDataPointDao(),
        AppDatabase.getInstance(application).paymentDao(),
        AppDatabase.getInstance(application).doseDao(),
        AppDatabase.getInstance(application).messageDao(),
        AppDatabase.getInstance(application).alertDao()
    )

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val loginResult: LiveData<Result<String>> = repository.loginResult

    val localUsers: LiveData<Int> = repository.getLocalUserCount()

    init {

        repository.checkLogin()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, HashUtils.sha512(password))
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
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

    private fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    fun resetDb() {
        viewModelScope.launch {
            repository.resetDb()
        }
    }

}
