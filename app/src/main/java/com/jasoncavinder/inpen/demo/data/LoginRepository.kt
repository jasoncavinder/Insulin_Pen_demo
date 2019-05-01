package com.jasoncavinder.inpen.demo.data

import android.util.Log
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.data.entities.user.UserDao
import com.jasoncavinder.inpen.demo.login.CreatedUser
import com.jasoncavinder.inpen.demo.login.LoggedInUser
import com.jasoncavinder.inpen.demo.login.Result
import com.jasoncavinder.inpen.demo.utilities.HashUtils
import java.io.IOException

class LoginRepository private constructor(private val _userDao: UserDao) {

    fun localUsers() = _userDao.countUsers()

    var user: LoggedInUser? = null
        private set

    var newUser: CreatedUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user?.userId?.let { _userDao.logout(it) }
        user = null
    }

    fun login(e: String, p: String): Result<LoggedInUser> {
        val testResult1 = _userDao.login()
        Log.d("LoginRepository", "login() returns:\n$testResult1\n${testResult1.value}")
        val testResult2 = _userDao.login(email = e)
        Log.d("LoginRepository", "login(e) returns:\n$testResult2\n${testResult2.value}")
        val debugResult = _userDao.login(e, HashUtils.sha512(p))
        Log.d("LoginRepository", "login(e, p) returns $debugResult\n${debugResult.value}")
        val result = _userDao.login(e, HashUtils.sha512(p)).value.run {
            Log.d("Login Repository", "login() result.value = $this")
            when (this) {
                null -> Result.Error(IOException("Invalid email or password.\n Please try again."))
                else -> Result.Success(LoggedInUser(userID, email, firstName, lastName))
            }
        }
        if (result is Result.Success) this.user = result.data
        return result
    }

    fun createUser(fname: String, lname: String, e: String, p: String): Result<CreatedUser> {
        return _userDao.createUser(
            User(
                email = e,
                firstName = fname,
                lastName = lname,
                password = HashUtils.sha512(p)
            )
        ).run {
            when (this) {
                null -> Result.Error(IOException("Failed to create account. Does your account already exist?"))
                else -> Result.Success(CreatedUser(userID, email, firstName, lastName, providerID, penID))
            }
        }
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
