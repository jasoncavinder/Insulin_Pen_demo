package com.jasoncavinder.old_inpen.demo.replaced.login

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
