//package com.jasoncavinder.inpen_demo.z.data
//
//import com.jasoncavinder.inpen_demo.z.data.model.LoggedInUser
//import java.io.IOException
//import com.jasoncavinder.inpen_demo.z.ui.ui.Result
//
///**
// * Class that handles authentication w/ ui credentials and retrieves user information.
// */
//class LoginDataSource {
//
//    fun ui(username: String, password: String): Result<LoggedInUser> {
//        try {
//            // TODO: handle loggedInUser authentication
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
//            return Result.Success(fakeUser)
//        } catch (e: Throwable) {
//            return Result.Error(IOException("Error logging in", e))
//        }
//    }
//
//    fun logout() {
//        // TODO: revoke authentication
//    }
//}
//
