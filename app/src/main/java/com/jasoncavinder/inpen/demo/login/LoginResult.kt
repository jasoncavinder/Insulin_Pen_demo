package com.jasoncavinder.inpen.demo.login

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
