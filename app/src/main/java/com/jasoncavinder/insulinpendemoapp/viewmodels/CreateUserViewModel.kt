/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.ui.login.CreateUserFormState
import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CreateUserViewModel internal constructor(
    application: Application
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

    private val _createUserForm = MutableLiveData<CreateUserFormState>()
    val createUserFormState: LiveData<CreateUserFormState> = _createUserForm

    private val _createUserResult = MutableLiveData<Result<String>>()
    val createUserResult: LiveData<Result<String>> = _createUserResult

    val addPenResult: LiveData<Result<Pen>> = repository.addPenResult

    private val _providerList = repository.getProviders()

    var provider: LiveData<Provider> = Transformations.switchMap(_providerList) {
        // TODO: handle empty list
        MutableLiveData<Provider>(it.shuffled().first())
    }

    val changeProviderResult: LiveData<Result<Provider>> = repository.changeProviderResult

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

    fun createUserDataChanged(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirm: String
    ) {
        when {
            !isNameValid(firstName) ->
                _createUserForm.value =
                    CreateUserFormState(firstNameError = R.string.invalid_first_name)
            !isNameValid(lastName) ->
                _createUserForm.value =
                    CreateUserFormState(lastNameError = R.string.invalid_last_name)
            !isEmailValid(email) ->
                _createUserForm.value =
                    CreateUserFormState(emailError = R.string.invalid_email)
            !isPasswordValid(password) ->
                _createUserForm.value =
                    CreateUserFormState(passwordError = R.string.invalid_password)
            !isConfirmValid(password, confirm) ->
                _createUserForm.value =
                    CreateUserFormState(confirmError = R.string.invalid_confirm)
            else ->
                _createUserForm.value = CreateUserFormState(isDataValid = true)
        }
    }

    fun createUser(firstName: String, lastName: String, email: String, password: String) =
        viewModelScope.launch {
            val user =
                User(email = email, firstName = firstName, lastName = lastName, password = HashUtils.sha512(password))
            withContext(IO) {
                _createUserResult.postValue(repository.createUser(user))
            }
        }

    fun addPen(pen: Pen) {
//        pen.userId = repository.user.id
        viewModelScope.launch {
            withContext(IO) {
                repository.addPen(pen)
            }
        }
    }

    fun changeProvider(providerId: String, userId: String) {
        viewModelScope.launch {
            withContext(IO) {
                repository.changeProvider(providerId, userId)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}

