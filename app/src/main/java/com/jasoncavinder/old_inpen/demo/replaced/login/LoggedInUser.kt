package com.jasoncavinder.old_inpen.demo.replaced.login

data class LoggedInUser(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String
) {
    fun asView() = LoggedInUserView(email, firstName, lastName)
}