/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider

class MessageList(messageList: LiveData<List<Message>>, provider: LiveData<Provider>) :
    MediatorLiveData<List<Message>>() {
    private var providerId: String = ""
    private var messages: List<Message> = listOf()

    init {
        addSource(messageList) {
            messages = it
            update()
        }
        addSource(provider) {
            providerId = it.providerId
            update()
        }
    }

    private fun update() {
        value = messages.apply {
            if (providerId.isNotBlank())
                filter { it.providerId == providerId }
            sortedByDescending { it.timeStamp }
        }
    }
}

class TwoSourceMediatorLiveData<A, B>(a: LiveData<A>, b: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
    private val TAG by lazy { this::class.java.simpleName }
    init {
        value = a.value to b.value
        addSource(a) { value = it to b.value }
        addSource(b) { value = a.value to it }

    }
}