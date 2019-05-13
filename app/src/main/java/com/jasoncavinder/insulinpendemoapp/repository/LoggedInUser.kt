/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.repository

import com.jasoncavinder.insulinpendemoapp.utilities.DEFAULT_USER_TIMEOUT_MINUTES
import java.util.*

class LoggedInUser(userId: String = "") : Observable() {

    private var _expires: Calendar = Calendar.getInstance()
    private var _userId: String = ""

    init {
        _userId = userId
        _expires = Calendar.getInstance()
    }

    private fun setUserId(newId: String) {
        _userId = newId
        setChanged()
        notifyObservers(_userId)
    }

    private fun update() {
        _expires = Calendar.getInstance()
        _expires.add(Calendar.MINUTE, DEFAULT_USER_TIMEOUT_MINUTES)
    }

    fun isLoggedIn(): Boolean {
        when (_expires.after(Calendar.getInstance())) {
            true -> {
                update()
                setChanged()
                notifyObservers(_userId)
                return true
            }
            false -> {
                setUserId("")
                return false
            }
        }
    }

    var id: String
        get() = if (isLoggedIn()) _userId else ""
        set(id) {
            setUserId(id)
            if (_userId.isNotBlank()) update()
        }

    fun logout() {
        _expires = Calendar.getInstance()
        setUserId("")
    }
}

