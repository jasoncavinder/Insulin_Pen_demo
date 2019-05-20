/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel internal constructor(
    application: Application
) : AndroidViewModel(application) {

    private val TAG by lazy { this::class.java.simpleName }

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
    val loginResult: LiveData<Result<String>> = repository.loginResult
    val updateUserResult: LiveData<Result<User>> = repository.updateUserResult
    val updatePaymentResult: LiveData<Result<Payment>> = repository.updatePaymentResult

    private var userId: LiveData<String> =
        Transformations.switchMap(repository.userIdLiveData) { MutableLiveData<String>(it) }

    var userProfile: LiveData<UserProfile> =
        Transformations.switchMap(userId) { repository.loadUserProfile(it) }

    var messages: LiveData<List<Message>> =
        Transformations.switchMap(userId) { repository.loadMessages(it) }

    var unreadMessages: LiveData<List<MessageSummary>> =
        Transformations.map(messages) { messageList ->
            createMessageSummaryList(messageList)
        }
    var noMessages: LiveData<Boolean> =
        Transformations.map(unreadMessages) { unreadList ->
            unreadList.size == 0
        }
    private val _providers: LiveData<List<Provider>> = repository.getProviders()
    var providers: MutableMap<String, String> = mutableMapOf()

    init {
        try {

        } catch (ex: java.lang.Exception) {
            Log.d(TAG, "Failed loading demoMessages", ex)
        }
        _providers.observeForever {
            for (provider in it) {
                providers.putIfAbsent(provider.providerId, provider.name)
            }
        }
    }

    fun updateUserData(
        firstName: String?, lastName: String?, email: String?,
        locationCity: String?, locationState: String?
    ) {
        viewModelScope.launch {
            repository.updateUser(
                firstName = if (firstName.isNullOrBlank()) null else firstName,
                lastName = if (lastName.isNullOrBlank()) null else lastName,
                email = if (email.isNullOrBlank()) null else email,
                locationCity = if (locationCity.isNullOrBlank()) null else locationCity,
                locationState = if (locationState.isNullOrBlank()) null else locationState
            )
        }
    }

    fun changePassword(password: String) {
        viewModelScope.launch {
            repository.updateUser(passwordHash = HashUtils.sha512(password))
        }
    }

    fun addPaymentMethod(type: Int, ccnum: Long?, ccexp: Int?, ccname: String?, email: String?) {
        viewModelScope.launch {
            repository.addPaymentMethod(
                Payment(
                    userId = userId.value!!, type = type,
                    ccnum = ccnum, ccexp = ccexp, ccname = ccname, email = email
                )
            )
        }
    }

    fun updatePaymentMethod(
        type: Int,
        ccnum: Long? = null,
        ccexp: Int? = null,
        ccname: String? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            repository.updatePaymentMethod(
                Payment(
                    userId = userId.value!!, type = type,
                    ccnum = ccnum, ccexp = ccexp, ccname = ccname, email = email
                )
            )
        }
    }

    fun verifyLogin() = repository.checkLogin()

    data class MessageSummary(
        val timestamp: Long,
        val from: String,
        val content: String
    )

    fun createMessageSummaryList(list: List<Message>): List<MessageSummary> {

        val messagesVM = ArrayList<MessageSummary>()

        var messageSummary: MessageSummary

        for (message in list) {
            messageSummary = MessageSummary(
                message.timeStamp,
                if (message.sent) "Me" else message.providerId, //TODO: get provider name
                if (message.content.length < 60) message.content else message.content.substring(0..60) + "..."
            )
            messagesVM.add(messageSummary)
        }
        return messagesVM
    }

    fun newMessage(message: Message) {
        viewModelScope.launch {
            withContext(IO) {
                repository.createMessage(message)
            }
        }
    }
}
