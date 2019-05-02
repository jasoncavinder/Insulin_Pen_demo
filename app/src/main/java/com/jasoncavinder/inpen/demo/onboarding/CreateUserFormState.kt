package com.jasoncavinder.inpen.demo.onboarding


data class CreateUserFormState(
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmError: Int? = null,
    val isDataValid: Boolean = false
)
