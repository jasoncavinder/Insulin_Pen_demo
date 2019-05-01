package com.jasoncavinder.inpen.demo.login

data class CreateUserResult(
    val success: CreatedUserView? = null,
    val error: Int? = null
)
