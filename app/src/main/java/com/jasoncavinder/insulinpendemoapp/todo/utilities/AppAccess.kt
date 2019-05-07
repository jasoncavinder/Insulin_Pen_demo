/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.todo.utilities

import java.util.*

class AppAccess {

    fun user() = userID

    fun authorize(uuid: UUID, minutes: Int = 15) {
        userID = uuid
        isAuth = true
        expires = System.currentTimeMillis() + (minutes * 60000)
    }

    fun deAuthorize() {
        userID = null
        isAuth = false
        expires = 0
    }

    fun isAuthorized(uuid: UUID? = userID): Boolean {
        return if (!isAuth ||
            userID != uuid ||
            expires < System.currentTimeMillis()
        )
            false.also { deAuthorize() }
        else true
    }

    companion object {
        private var userID: UUID? = null

        private var isAuth: Boolean = false

        private var expires: Long = System.currentTimeMillis()
    }

}