/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.old_inpen.demo.replaced.data

//package com.jasoncavinder.inpen.demo.data
//
//import androidx.annotation.WorkerThread
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.jasoncavinder.inpen.demo.replaced.data.entities.TransactionDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.penPoints.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.penPoints.PenDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
//import com.jasoncavinder.inpen.demo.replaced.login.LoggedInUser
//import com.jasoncavinder.inpen.demo.replaced.login.Result
//import com.jasoncavinder.inpen.demo.onboarding.CreatedUser
//import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import java.io.IOException
//
//class LoginRepository private constructor(
//    private val _userDao: UserDao,
//    private val _penDao: PenDao,
//    private val _providerDao: ProviderDao,
//) {
//
//    private val TAG by lazy { this::class.java.simpleName }
//
//    private val _user = MutableLiveData<User>()
//    val user: LiveData<User> = _user
//
//    val localUsers: LiveData<Int> = _userDao.countUsers()
//
//    private val _allProviders = MutableLiveData<List<Provider>>()
//    val allProviders: LiveData<List<Provider>> = _allProviders
//
//    var loggedInUser: LoggedInUser? = null
//        private set
//
//    var newUser: CreatedUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = loggedInUser != null
//
//    init {
//        loggedInUser = null
//        _allProviders.value = _providerDao.getProviders()
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun logout() {
//        loggedInUser?.userId?.let { _userDao.logout(it) }
//        loggedInUser = null
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun login(e: String, p: String): Result<LoggedInUser> {
//        val result = _userDao.login(e, HashUtils.sha512(p)).run {
//            when (this) {
//                null -> Result.Error(IOException("Invalid email or password.\n Please try again."))
//                else -> Result.Success(LoggedInUser(userID, email, firstName, lastName))
//            }
//        }
//        if (result is Result.Success) this.loggedInUser = result.data
//        return result
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun createUser(fname: String, lname: String, e: String, p: String): Result<CreatedUser> {
//        val result = _userDao.createUser(
//            User(
//                email = e,
//                firstName = fname,
//                lastName = lname,
//                password = HashUtils.sha512(p)
//            )
//        ).run {
//            when (this) {
//                null -> Result.Error(IOException("Failed to create account. Does your account already exist?"))
//                else -> Result.Success(CreatedUser(userID, email, firstName, lastName, providerID, penID))
//            }
//        }
//        if (result is Result.Success) this.newUser = result.data
//        return result
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun addPenToUser(userID: String, penPoints: Pen): Boolean {
//        var success: Boolean
//        try {
//            _transactionDao.addPenToUser(userID, penPoints)
//            success = true
//        } catch (e: IOException) {
//            success = false
//        }
//        return success
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun addProvider(providerID: String) {
//        GlobalScope.launch {
//            try {
//                val updatedUser = _user.value!!.run { this.providerID = providerID; this }
//                val rowsUpdated = _userDao.update(updatedUser)
//                when (rowsUpdated) {
//                    1 -> _user = updatedUser
//                    -1, 0 ->
//                    else -> TODO() // impossible!?
//
//                }
//            } catch (e: NullPointerException) {
//                TODO() // Log and react
//            } catch (e: Exception) { // any other exception
//                TODO() // Log
//            }
//        }
//    }
//
//    companion object {
//
//        @Volatile
//        private var _instance: LoginRepository? = null
//
//        fun getInstance(userDao: UserDao, penDao: PenDao, providerDao: ProviderDao, transactionDao: TransactionDao) =
//            _instance ?: synchronized(this) {
//                _instance ?: LoginRepository(userDao, penDao, providerDao, transactionDao).also { _instance = it }
//            }
//    }
//}
