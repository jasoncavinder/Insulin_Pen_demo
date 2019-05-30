/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.message

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider

class MessageList(messageList: LiveData<List<Message>>, provider: LiveData<Provider>, context: Context?) :
    MediatorLiveData<List<Message>>() {
    private var providerId: String = ""
    private var messages: List<Message> = listOf()
    var userPhoto: Int? = R.drawable.ic_account_circle_black_24dp
    var providerPhoto: Int? = null
        private set

    init {
        addSource(messageList) {
            messages = it
            update()
        }
        addSource(provider) {
            providerId = it.providerId
            providerPhoto = context?.resources?.getIdentifier(it.photo, "raw", context.packageName)
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
