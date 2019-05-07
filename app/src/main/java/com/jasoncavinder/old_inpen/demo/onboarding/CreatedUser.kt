package com.jasoncavinder.old_inpen.demo.onboarding

data class CreatedUser(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val providerId: String? = null,
    val penId: String? = null
)
//{
//    fun asView() = CreatedUserView(firstName, lastName, email)
//}