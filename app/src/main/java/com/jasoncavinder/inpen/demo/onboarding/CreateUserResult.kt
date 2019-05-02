package com.jasoncavinder.inpen.demo.onboarding

data class CreateUserResult(
    val success: CreatedUser? = null,
    val error: Int? = null
)
