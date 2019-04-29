package com.jasoncavinder.inpen_demo.persistence

import androidx.lifecycle.LiveData
import com.jasoncavinder.inpen.demo.data.entities.user.User
import java.util.*

interface LoginDataSource {

    fun getUserByID(userID: String): LiveData<Optional<User>>

    fun insertOrUpdateUser(user: User)

    fun deleteAllUsers()

    fun login(email: String, password: String): Result<LiveData<User>>

    fun logout(userID: String)

    fun countUsers(): LiveData<Int>
}
/**
 * Class that handles authentication w/ ui credentials and retrieves user information.

class LoginDataSource() {

fun ui(username: String, password: String): Result<User> {
try {
// TODO: handle loggedInUser authentication
throw Throwable()
//            val fakeUser = User(java.util.UUID.randomUUID().toString(), "Jane Doe")
//
//            return Result.Success(fakeUser)
} catch (e: Throwable) {
return Result.Error(IOException("Error logging in", e))
}
}

fun logout(userID) {
// TODO: revoke authentication
}

}
 */
