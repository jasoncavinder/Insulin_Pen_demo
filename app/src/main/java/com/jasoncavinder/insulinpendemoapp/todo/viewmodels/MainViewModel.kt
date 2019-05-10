/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.todo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
//import com.jasoncavinder.insulinpendemoapp.DemoApplication
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.utilities.Result

class MainViewModel internal constructor(
//    application: DemoApplication,
    application: Application
//    appRepository: AppRepository
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

    init {
        repository.checkLogin()
    }


}