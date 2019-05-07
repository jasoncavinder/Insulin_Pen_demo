///*
// * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
// * This project is licenced to the client of Upwork contract #21949291. It is not
// * licensed for public use. See the LICENSE.md file for details
// */
//
//package com.jasoncavinder.inpen.demo.replaced.data
//
//import android.os.Bundle
//import androidx.lifecycle.LiveData
//import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
//import com.jasoncavinder.inpen.demo.replaced.data.entities.user.UserProfileDao
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.withContext
//
//class UserRepository private constructor(
//    private val _userDao: UserDao,
//    private val _providerDao: ProviderDao,
//    private val _penDao: PenDao,
//    private val _penDataPointDao: PenDataPointDao,
//    private val _doseDao: DoseDao,
//    private val _userProfileDao: UserProfileDao
//) {
//    private val TAG by lazy { this::class.java.simpleName }
//
//
//    private var cache: Bundle = Bundle.EMPTY
//
//    fun getLocalUserCount() = _userDao.countUsers()
//
//    fun getProviders() = _providerDao.getProviders()
//
//    fun verifyUser(email: String, password: String) = _userDao.login(email, password)
//        .also { cache.putString("userID", it) }
//
//    suspend fun logout() {
//        withContext(IO) {
//            return@withContext
//            @Suppress("UNREACHABLE_CODE")
//            TODO(reason = "Not Implemented: Perform any logout login on the database")
//        }
//    }
//
//    suspend fun createUser(user: User): Long? {
//        cache.putString("userID", user.userID)
//        return withContext(IO) {
//            return@withContext _userDao.insertReplace(user)
//        }
//    }
//
//    suspend fun addPen(pen: Pen): Long? {
//        return withContext(IO) {
//            return@withContext _penDao.insertReplace(pen)
//        }
//    }
//
//    suspend fun changeProvider(user: String, provider: String): Long? {
//        return withContext(IO) {
//            return@withContext _userDao.updateProvider(user, provider)
//        }
//    }
//
//    suspend fun loadUserProfile(userID: String? = null): LiveData<UserProfile>? {
//        return withContext(IO) {
//            return@withContext (when (userID) {
//                null -> cache.getString("userID")
//                else -> userID
//            })?.run {
//                _userProfileDao.loadUserProfile(this)?.also {
//                    cache.putString("userID", this)
//                }
//            }
//        }
//    }
//
//    fun recoverLoggedInId(): String? {
//        return cache.getString("userID")?.let {
//            when (_userDao.loggedIn(it)) {
//                true -> it
//                false -> null
//            }
//        }
//    }
//
//    fun loggedIn(): Boolean = cache.getString("userID").run {
//        return when (this) {
//            null -> false
//            else -> _userDao.loggedIn(this)
//        }
//    }
//
//    companion object {
//
//        @Volatile
//        private var _instance: UserRepository? = null
//
//        fun getInstance(
//            userDao: UserDao,
//            providerDao: ProviderDao,
//            penDao: PenDao,
//            penDataPointDao: PenDataPointDao,
//            doseDao: DoseDao,
//            userProfileDao: UserProfileDao
//        ) =
//            _instance ?: synchronized(this) {
//                _instance
//                    ?: UserRepository(
//                        _userDao = userDao,
//                        _providerDao = providerDao,
//                        _penDao = penDao,
//                        _penDataPointDao = penDataPointDao,
//                        _doseDao = doseDao,
//                        _userProfileDao = userProfileDao
//                    ).also { _instance = it }
//            }
//    }
//
//}