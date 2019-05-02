package com.jasoncavinder.inpen.demo.onboarding

data class CreateUserResult(
    val success: CreatedUserView? = null,
    val error: Int? = null
)
