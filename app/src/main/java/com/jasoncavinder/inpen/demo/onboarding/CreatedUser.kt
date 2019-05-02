package com.jasoncavinder.inpen.demo.onboarding

data class CreatedUser(
    val userID: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val providerID: String? = null,
    val penID: String? = null
)
//{
//    fun asView() = CreatedUserView(firstName, lastName, email)
//}