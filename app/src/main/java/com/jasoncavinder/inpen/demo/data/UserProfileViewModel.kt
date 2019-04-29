package com.jasoncavinder.inpen.demo.data

class UserProfileViewModel {
    private val SESSION_TIME_MINUTES: Int = 15

    private var sessionExpires: Long = 0L

    private fun extendSession() {
        sessionExpires = System.currentTimeMillis() + SESSION_TIME_MINUTES.times(60000L)
    }

    init {
        sessionExpires = 0L

    }

}
