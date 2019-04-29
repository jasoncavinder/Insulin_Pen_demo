package com.jasoncavinder.inpen.demo.data

import com.jasoncavinder.inpen.demo.data.entities.user.UserDao
import com.jasoncavinder.inpen.demo.login.LoggedInUser
import com.jasoncavinder.inpen.demo.login.Result
import com.jasoncavinder.inpen.demo.utilities.HashUtils
import java.io.IOException

class LoginRepository private constructor(private val _userDao: UserDao) {

    fun localUsers() = _userDao.countUsers()

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
//        _userDao.logout()
    }

    fun login(e: String, p: String): Result<LoggedInUser> {
        val result = _userDao.login(e, HashUtils.sha512(p)).value.run {
            when (this) {
                null -> Result.Error(IOException("Invalid email or password.\n Please try again."))
                else -> Result.Success(LoggedInUser(userID, email, firstName, lastName))
            }
        }
        if (result is Result.Success) this.user = result.data
        return result
    }


    companion object {

        @Volatile
        private var _instance: LoginRepository? = null

        fun getInstance(userDao: UserDao) =
            _instance ?: synchronized(this) {
                _instance ?: LoginRepository(userDao).also { _instance = it }
            }
    }
}
