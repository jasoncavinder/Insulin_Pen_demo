package com.jasoncavinder.inpen.demo.login

/**
 * Data validation state of the ui form.
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
