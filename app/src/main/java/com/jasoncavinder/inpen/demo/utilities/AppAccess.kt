package com.jasoncavinder.inpen.demo.utilities

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