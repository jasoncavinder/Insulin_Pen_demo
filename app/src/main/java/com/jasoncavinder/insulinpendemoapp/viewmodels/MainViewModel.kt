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
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
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
        AppDatabase.getInstance(application).doseDao(),
        AppDatabase.getInstance(application).messageDao(),
        AppDatabase.getInstance(application).alertDao()
    )
    val loginResult: LiveData<Result<String>> = repository.loginResult

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
