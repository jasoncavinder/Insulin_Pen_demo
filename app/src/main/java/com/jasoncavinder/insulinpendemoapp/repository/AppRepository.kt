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
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageDao
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.PaymentDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenWithDataPoints
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class AppRepository private constructor(
    private val userDao: UserDao,
    private val providerDao: ProviderDao,
    private val penDao: PenDao,
    private val penDataPointDao: PenDataPointDao,
    private val paymentDao: PaymentDao,
    private val doseDao: DoseDao,
    private val messageDao: MessageDao,
    private val alertDao: AlertDao
) {
    private val TAG by lazy { this::class.java.simpleName }
    /* Manage and Cache Login Status */
    var user = LoggedInUser()
        private set

    private val _userIdLiveData = MutableLiveData<String>()
    val userIdLiveData: LiveData<String> = _userIdLiveData

    private fun setLoggedIn(userId: String) {
        user.id = userId
    }

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val _updateUserResult = MutableLiveData<Result<User>>()
    val updateUserResult: LiveData<Result<User>> = _updateUserResult

    private val _updatePaymentResult = MutableLiveData<Result<Payment>>()
    val updatePaymentResult: LiveData<Result<Payment>> = _updatePaymentResult

    private val _changeProviderResult = MutableLiveData<Result<Provider>>()
    val changeProviderResult: LiveData<Result<Provider>> = _changeProviderResult

    private val _addPenResult = MutableLiveData<Result<Pen>>()
    val addPenResult: LiveData<Result<Pen>> = _addPenResult

    private val _changePenResult = MutableLiveData<Result<PenWithDataPoints>>()
    val changePenResult: LiveData<Result<PenWithDataPoints>> = _changePenResult

    private val _editDoseResult = MutableLiveData<Result<Dose>>()
    val editDoseResult: LiveData<Result<Dose>> = _editDoseResult

    private val _injectDoseResult = MutableLiveData<Result<Dose>>()
    val injectDoseResult: LiveData<Result<Dose>> = _injectDoseResult


    init {
        user.addObserver { _, id ->
            when (id) {
                is String -> {
                    _userIdLiveData.postValue(id)
                }
                else -> checkLogin()
            }
        }
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
        _loginResult.postValue(Result.Error(Exception("User logged out")))
    }


    /* Pass-through database (usually LiveData) */
    fun getLocalUserCount() = userDao.countUsers()

    fun loadUserProfile(userId: String) = userDao.getUserProfile(userId)

    fun loadMessages(userId: String) = messageDao.getUserMessages(userId)

    fun getProviders() = providerDao.getProviders()

    fun getPens() = penDao.getAllPens()

    fun getPen(penId: String) = penDao.getPenById(penId)

    fun getUserPen(userId: String = user.id) = penDao.getPenWithData(userId)

    fun getPenDataPoints(penId: String) = penDataPointDao.getDataPoints(penId)

    fun getPaymentMethod(userId: String = user.id) = paymentDao.getUserPayment(userId)

    fun getUserDoses(userId: String = user.id) = doseDao.getUserDoses(userId)

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

    suspend fun changeProvider(providerId: String, userId: String): Result<Int> {
        if (!(userId == user.id)) return Result.Error(Exception("User is not logged in."))
        var updates = 0
        withContext(IO) {
            try {
                updates = userDao.changeProvider(userId, providerId)
            } catch (e: Exception) {
                Log.e(TAG, "Exception changing randomProvider", e)
            }
        }
        return when (updates) {
            1 -> Result.Success(updates)
            else -> Result.Error(IOException("Could not change randomProvider."))
        }
    }

    suspend fun updateUser(
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        locationCity: String? = null,
        locationState: String? = null,
        passwordHash: String? = null,
        paymentConfigured: Boolean? = null,
        providerId: String? = null
    ) {
        withContext(IO) {
            try {
                val user: User = userDao.getUser(this@AppRepository.user.id)
                firstName?.let { user.firstName = it }
                lastName?.let { user.lastName = it }
                email?.let { user.email = it }
                locationCity?.let { user.locationCity = it }
                locationState?.let { user.locationState = it }
                passwordHash?.let { user.password = it }
                paymentConfigured?.let { user.paymentConfigured = it }
                providerId?.let { user.providerId = it }
                val result = userDao.update(user)
                if (result > 0) _updateUserResult.postValue(Result.Success<User>(user))
                else throw Exception("Could not update user.")
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to update user", ex)
                _updateUserResult.postValue(Result.Error(ex))
            }
        }
    }

    suspend fun addPaymentMethod(payment: Payment) {
        withContext(IO) {
            try {
                val result = paymentDao.insert(payment)
                if (result > 0) _updatePaymentResult.postValue(Result.Success<Payment>(payment))
                else throw Exception("Could not add paymentMethod method.")
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to add paymentMethod", ex)
                _updatePaymentResult.postValue(Result.Error(ex))
            }
        }
    }

    suspend fun updatePaymentMethod(payment: Payment) {
        withContext(IO) {
            try {
                val result = paymentDao.updateUserPayment(payment)
                if (result > 0) _updatePaymentResult.postValue(Result.Success<Payment>(payment))
                else throw Exception("Could not update paymentMethod method.")
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to update paymentMethod", ex)
                _updatePaymentResult.postValue(Result.Error(ex))
            }
        }
    }

    suspend fun updateDose(dose: Dose) {
        withContext(IO) {
            try {
                val result = doseDao.insertReplace(dose)
                if (result > 0) _editDoseResult.postValue(Result.Success(dose))
                else _editDoseResult.postValue(Result.Error(IOException("Could not edit dose(s)")))
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to edit dose(s)", ex)
            }
        }
    }

    suspend fun injectDose(dose: Dose) {
        withContext(IO) {
            try {
                val result =
                    doseDao.inject(dose.copy(givenAmount = dose.scheduledAmount, givenTime = Calendar.getInstance()))
                if (result == null) _injectDoseResult.postValue(Result.Error(IOException("Could not update dose history.")))
                else _injectDoseResult.postValue(Result.Success(result))
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to edit dose", ex)
            }
        }
    }

    suspend fun deleteDose(dose: Dose) {
        withContext(IO) { doseDao.delete(dose) }
    }

    suspend fun createMessage(message: Message) {
        withContext(IO) {
            messageDao.insertReplace(message)
        }
    }

    suspend fun resetDb() {
        withContext(IO) {
            userDao.deleteAllUsers()
            messageDao.deleteAllMessages()
            penDao.deleteAllPens()
            paymentDao.deleteAllPayments()
        }
    }

    suspend fun sendMessage(message: Message) {
        withContext(IO) {
            messageDao.insert(message)
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
            paymentDao: PaymentDao,
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
                        paymentDao = paymentDao,
                        doseDao = doseDao,
                        messageDao = messageDao,
                        alertDao = alertDao
                    ).also { instance = it }
            }
    }

}