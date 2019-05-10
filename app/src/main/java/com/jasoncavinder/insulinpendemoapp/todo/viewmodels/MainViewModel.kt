/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.todo.viewmodels

//import com.jasoncavinder.insulinpendemoapp.DemoApplication
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel internal constructor(
    application: Application
) : AndroidViewModel(application) {

    private val repository: AppRepository = AppRepository.getInstance(
        AppDatabase.getInstance(application).userDao(),
        AppDatabase.getInstance(application).providerDao(),
        AppDatabase.getInstance(application).penDao(),
        AppDatabase.getInstance(application).penDataPointDao(),
        AppDatabase.getInstance(application).doseDao(),
        AppDatabase.getInstance(application).messageDao(),
        AppDatabase.getInstance(application).alertDao()
    )

    val loginResult: LiveData<Result<String>> = repository.loginResult

    fun verifyLogin() = repository.checkLogin()

    //    private var _userProfileResult: LiveData<UserProfile>
    //    private val _userProfile = MediatorLiveData<UserProfile>()
    var userProfileResult: LiveData<Result<UserProfile>> = repository.loadProfileResult

    private val _userProfile = MutableLiveData<UserProfile>()
    var userProfile: LiveData<UserProfile> = _userProfile

    fun loadUserProfile() {
        viewModelScope.launch {
            withContext(IO) {
                repository.loadUserProfile()
            }
        }
    }

    init {

        repository.checkLogin()
        userProfileResult.observeForever {
            when (it) {
                is Result.Success -> {
                    _userProfile.postValue(it.data)
                }
            }
        }
    }


}