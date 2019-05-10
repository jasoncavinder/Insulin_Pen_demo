/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jasoncavinder.insulinpendemoapp.database.entities.alert.AlertDao
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.IOException

class AppRepository private constructor(
    private val userDao: UserDao,
    private val providerDao: ProviderDao,
    private val penDao: PenDao,
    private val penDataPointDao: PenDataPointDao,
    private val doseDao: DoseDao,
    private val messageDao: MessageDao,
    private val alertDao: AlertDao
) {
    private val TAG by lazy { this::class.java.simpleName }

    /* Manage and Cache Login Status */
    var user = LoggedInUser()
        private set

    private fun setLoggedIn(userId: String) {
        user.id = userId
    }

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val _addPenResult = MutableLiveData<Result<Pen>>()
    val addPenResult: LiveData<Result<Pen>> = _addPenResult

    private val _changeProviderResult = MutableLiveData<Result<User>>()
    val changeProviderResult: LiveData<Result<User>> = _changeProviderResult

    private val _loadProfileResult = MutableLiveData<Result<UserProfile>>()
    val loadProfileResult: LiveData<Result<UserProfile>> = _loadProfileResult

//    private val _userProfile = MediatorLiveData<UserProfile>()
//    var userProfile: LiveData<UserProfile> = _userProfile
//        private set

    init {
//        _userProfile.addSource(loginResult) {
//            when (it) {
//                is Result.Success -> {
//                    val result = async {
//
//                    }.await()
//                    _userProfile.postValue(userDao.getUserProfile(it.data))
//                }
//            }
//        }
    }

    fun checkLogin() {
        _loginResult.postValue(
            when (user.isLoggedIn()) {
                true -> Result.Success(user.id)
                false -> Result.Error(exception = Exception("Please Login"))
            }
        )
    }

    suspend fun login(email: String, passwordHash: String) {
        withContext(IO) {
            if (!user.isLoggedIn())
                user.id = userDao.getIdWithCredentials(email, passwordHash) ?: ""
            _loginResult.postValue(
                when (user.isLoggedIn()) {
                    true -> Result.Success(user.id)
                    false -> Result.Error(IOException("Invalid email/password combination"))
                }
            )
        }
    }

    suspend fun createUser(user: User): Result<String> {
        withContext(IO) {
            try {
                this@AppRepository.user.id = userDao.createUser(user)
            } catch (e: Exception) {
                Log.e(TAG, "Exception creating user", e)
                this@AppRepository.user.id = ""
            }
        }
        return when (this.user.isLoggedIn()) {
            true -> Result.Success(this.user.id).apply {
                this@AppRepository._loginResult.postValue(Result.Success(this.data))
            }
            false -> Result.Error(IOException("Could not create user. Does that account already exist?"))
        }
    }

    fun logout() {
        user.logout()
    }


    /* Pass-through database (usually LiveData) */
    fun getLocalUserCount() = userDao.countUsers()

    fun getProviders() = providerDao.getProviders()

    fun getPens() = penDao.getAllPens()

    fun getPen(penId: String) = penDao.getPenById(penId)

    fun getUserPen(userId: String = user.id) = penDao.getPenWithData(userId)

    fun getPenDataPoints(penId: String) = penDataPointDao.getDataPoints(penId)

    fun getUserDoses(userId: String = user.id) = doseDao.getUserDoses(userId)

    fun loadMessages(userId: String) = messageDao.getUserMessages(userId)

    fun loadAlerts(userId: String) = alertDao.getUserAlerts(userId)


    suspend fun addPen(pen: Pen) {
        if (!(pen.userId == user.id))
            _addPenResult.postValue(Result.Error(Exception("User is not logged in.")))
        withContext(IO) {
            try {
                penDao.addPen(pen).let { penId ->
                    penDao.getPenById(penId)
                }.run {
                    when (this) {
                        null -> _addPenResult.postValue(
                            Result.Error(
                                IOException(
                                    "Could not load pen. Please try again."
                                )
                            )
                        )
                        else -> _addPenResult.postValue(
                            Result.Success(this)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception adding pen", e)
                _addPenResult.postValue(Result.Error(IOException("Exception adding pen", e)))
            }
        }
    }

    suspend fun changeProvider(userId: String, provider: String): Result<Int> {
        if (!(userId == user.id)) return Result.Error(Exception("User is not logged in."))
        var updates = 0
        withContext(IO) {
            try {
                updates = userDao.changeProvider(userId, provider)
            } catch (e: Exception) {
                Log.e(TAG, "Exception changing provider", e)
            }
        }
        return when (updates) {
            1 -> Result.Success(updates)
            else -> Result.Error(IOException("Could not change provider."))
        }
    }


    suspend fun loadUserProfile() {
        _loadProfileResult.postValue(
            withContext(IO) {
                // user.id ?: return@withContext
                try {
                    return@withContext Result.Success(userDao.getUserProfile(user.id))
                } catch (ex: IOException) {
                    return@withContext Result.Error(Exception("Failed to load user profile", ex))
                }
            }
        )
    }
//    suspend fun loadUserProfile(): LiveData<Result<UserProfile>> {
////        return when {
////            (!(userId == user.id)) -> null
////            else -> {
////                var profile: LiveData<UserProfile>
//                return withContext(IO) {
////                    try {
////                        profile = userDao.getUserProfile(user.id)
////                    } catch (e: Exception) {
////                        Log.e(TAG, "Could not load user profile.", e)
////                    }
//                    return@withContext userDao.getUserProfile(user.id)
//                }
////                profile
////            }
////        }
//    }

    suspend fun resetDb() {
        withContext(IO) {
            penDao.deleteAllPens()
            userDao.deleteAllUsers()
        }
    }

    companion object {

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(
            userDao: UserDao,
            providerDao: ProviderDao,
            penDao: PenDao,
            penDataPointDao: PenDataPointDao,
            doseDao: DoseDao,
            messageDao: MessageDao,
            alertDao: AlertDao
        ) =
            instance ?: synchronized(this) {
                instance
                    ?: AppRepository(
                        userDao = userDao,
                        providerDao = providerDao,
                        penDao = penDao,
                        penDataPointDao = penDataPointDao,
                        doseDao = doseDao,
                        messageDao = messageDao,
                        alertDao = alertDao
                    ).also { instance = it }
            }
    }

}