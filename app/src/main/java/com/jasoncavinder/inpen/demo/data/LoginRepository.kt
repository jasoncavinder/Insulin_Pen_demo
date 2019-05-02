package com.jasoncavinder.inpen.demo.data

import com.jasoncavinder.inpen.demo.data.entities.TransactionDao
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import com.jasoncavinder.inpen.demo.data.entities.pen.PenDao
import com.jasoncavinder.inpen.demo.data.entities.provider.ProviderDao
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.data.entities.user.UserDao
import com.jasoncavinder.inpen.demo.login.LoggedInUser
import com.jasoncavinder.inpen.demo.login.Result
import com.jasoncavinder.inpen.demo.onboarding.CreatedUser
import com.jasoncavinder.inpen.demo.utilities.HashUtils
import java.io.IOException

class LoginRepository private constructor(
    private val _userDao: UserDao,
    private val _penDao: PenDao,
    private val _providerDao: ProviderDao,
    private val _transactionDao: TransactionDao
) {

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
        val result = _userDao.login(e, HashUtils.sha512(p)).run {
            when (this) {
                null -> Result.Error(IOException("Invalid email or password.\n Please try again."))
                else -> Result.Success(LoggedInUser(userID, email, firstName, lastName))
            }
        }
        if (result is Result.Success) this.user = result.data
        return result
    }

    fun createUser(fname: String, lname: String, e: String, p: String): Result<CreatedUser> {
        val result = _userDao.createUser(
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
        if (result is Result.Success) this.newUser = result.data
        return result
    }

    fun addPenToUser(userID: String, pen: Pen): Boolean {
        var success: Boolean
        try {
            _transactionDao.addPenToUser(userID, pen)
            success = true
        } catch (e: IOException) {
            success = false
        }
        return success

    }

    companion object {

        @Volatile
        private var _instance: LoginRepository? = null

        fun getInstance(userDao: UserDao, penDao: PenDao, providerDao: ProviderDao, transactionDao: TransactionDao) =
            _instance ?: synchronized(this) {
                _instance ?: LoginRepository(userDao, penDao, providerDao, transactionDao).also { _instance = it }
            }
    }
}
