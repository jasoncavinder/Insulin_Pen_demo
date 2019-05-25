/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseType
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.PaymentType
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenWithDataPoints
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.repository.AppRepository
import com.jasoncavinder.insulinpendemoapp.ui.login.CreateUserFormState
import com.jasoncavinder.insulinpendemoapp.ui.login.LoginFormState
import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


/*
Educational Note:

This application's class is 'DemoApplication' which extends 'Application'.
The only difference is to preload some photos from res to internal storage
for demo purposes. The real version of this app would load those resources
from an online resource.

Here we are telling android to build the viewmodel using default android
factory for Application and NOT DemoApplication. This is necessary because
android of course does not supply a custom factory for our custom
application class, and this works because our extension of Application
doesn't modify anything that would affect viewmodels.

So, although it may seem natural to put our actual 'DemoApplication' class
here, DON'T!

~ Jason
 */
class MainViewModel internal constructor(
    application: Application /* !!! NOT DemoApplication !!! */
) : AndroidViewModel(application) {

    private val TAG by lazy { this::class.java.simpleName }

    private val repository: AppRepository = AppRepository.getInstance(
        AppDatabase.getInstance(application).userDao(),
        AppDatabase.getInstance(application).providerDao(),
        AppDatabase.getInstance(application).penDao(),
        AppDatabase.getInstance(application).penDataPointDao(),
        AppDatabase.getInstance(application).paymentDao(),
        AppDatabase.getInstance(application).doseDao(),
        AppDatabase.getInstance(application).messageDao(),
        AppDatabase.getInstance(application).alertDao()
    )

    val localUsers: LiveData<Int> = repository.getLocalUserCount()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    val loginResult: LiveData<Result<String>> = repository.loginResult

    private val _createUserForm = MutableLiveData<CreateUserFormState>()
    val createUserFormState: LiveData<CreateUserFormState> = _createUserForm
    private val _createUserResult = MutableLiveData<Result<String>>()
    val createUserResult: LiveData<Result<String>> = _createUserResult

    private val _providerList = repository.getProviders()
    var providers: LiveData<Map<String, String>> =
        Transformations.switchMap(_providerList) {
            MutableLiveData<Map<String, String>>().apply {
                value = run {
                    val map = mutableMapOf<String, String>()
                    for (provider in it) {
                        map.putIfAbsent(provider.providerId, provider.name)
                    }
                    map
                }
            }
        }
    var randomProvider: LiveData<Provider> = Transformations.switchMap(_providerList) {
        // TODO: handle empty list
        MutableLiveData<Provider>(it.shuffled().first())
    }


    val updateUserResult: LiveData<Result<User>> = repository.updateUserResult
    val updatePaymentResult: LiveData<Result<Payment>> = repository.updatePaymentResult
    val addPenResult: LiveData<Result<Pen>> = repository.addPenResult
    val changeProviderResult: LiveData<Result<Provider>> = repository.changeProviderResult
    val changePenResult: LiveData<Result<PenWithDataPoints>> = repository.changePenResult
    val editDoseResult: LiveData<Result<Dose>> = repository.editDoseResult


    var userId: LiveData<String> =
        Transformations.switchMap(repository.userIdLiveData) { MutableLiveData<String>(it) }

    private val _userProfile: LiveData<UserProfile> =
        Transformations.switchMap(userId) { repository.loadUserProfile(it) }

    var user: MediatorLiveData<User> = MediatorLiveData()
    var paymentMethod: MediatorLiveData<Payment> = MediatorLiveData()
    var provider: MediatorLiveData<Provider> = MediatorLiveData()
    var pen: MediatorLiveData<PenWithDataPoints> = MediatorLiveData()

    var messages: LiveData<List<Message>> =
        Transformations.switchMap(userId) { repository.loadMessages(it) }

    var unreadMessages: LiveData<List<MessageSummary>> =
        Transformations.map(messages) { messageList ->
            createMessageSummaryList(messageList)
        }
    var noMessages: LiveData<Boolean> =
        Transformations.map(unreadMessages) { unreadList ->
            unreadList.size == 0
        }

    var doses: LiveData<List<Dose>> =
        Transformations.switchMap(userId) { repository.getUserDoses(it) }

    var nextBasalDose: MediatorLiveData<Dose> = MediatorLiveData()
    var nextBolusDose: MediatorLiveData<Dose> = MediatorLiveData()

    init {

        repository.checkLogin()

        user.addSource(_userProfile) {
            it?.let { user.value = it.user }
        }
        user.addSource(updateUserResult) {
            when (it) {
                is Result.Success -> user.value = it.data
            }
        }
        paymentMethod.addSource(_userProfile) {
            it.paymentMethod?.let { set -> if (set.isNotEmpty()) paymentMethod.value = set.first() }
        }
        paymentMethod.addSource(updatePaymentResult) {
            when (it) {
                is Result.Success -> paymentMethod.value = it.data
            }
        }
        provider.addSource(_userProfile) {
            it?.provider?.let { set -> if (set.isNotEmpty()) provider.value = set.first() }
        }
        provider.addSource(changeProviderResult) {
            when (it) {
                is Result.Success -> provider.value = it.data
            }
        }
        pen.addSource(_userProfile) {
            it.pen?.let { set -> if (set.isNotEmpty()) pen.value = set.first() }
        }
        pen.addSource(changePenResult) {
            when (it) {
                is Result.Success -> pen.value = it.data
            }
        }
        nextBasalDose.addSource(doses) {
            nextBasalDose.value = it
                .sortedByDescending { it.scheduledTime }
                .firstOrNull { dose ->
                    (dose.type == DoseType.BASAL && dose.scheduledTime?.after(Calendar.getInstance()) ?: false).apply {
                        Log.d(
                            TAG,
                            "dose.scheduledTime = ${dose.scheduledTime} and Calendar.getInstance() = ${Calendar.getInstance()}"
                        )
                    }
                }
        }
        nextBolusDose.addSource(doses) {
            nextBolusDose.value = it.sortedByDescending { dose -> dose.createdTime }
                .firstOrNull { dose -> dose.type == DoseType.BOLUS }
        }

//        try {
//
//        } catch (ex: java.lang.Exception) {
//            Log.d(TAG, "Failed loading demoMessages", ex)
//        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, HashUtils.sha512(password))
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    fun updateUserData(
        firstName: String?, lastName: String?, email: String?,
        locationCity: String?, locationState: String?
    ) {
        viewModelScope.launch {
            repository.updateUser(
                firstName = if (firstName.isNullOrBlank()) null else firstName,
                lastName = if (lastName.isNullOrBlank()) null else lastName,
                email = if (email.isNullOrBlank()) null else email,
                locationCity = if (locationCity.isNullOrBlank()) null else locationCity,
                locationState = if (locationState.isNullOrBlank()) null else locationState
            )
        }
    }

    fun changePassword(password: String) =
        viewModelScope.launch { repository.updateUser(passwordHash = HashUtils.sha512(password)) }

    fun addPaymentMethod(type: PaymentType, ccnum: Long?, ccexp: Int?, ccname: String?, email: String?) {
        viewModelScope.launch {
            repository.addPaymentMethod(
                Payment(
                    userId = userId.value!!, type = type,
                    ccnum = ccnum, ccexp = ccexp, ccname = ccname, email = email
                )
            )
        }
    }

    fun updatePaymentMethod(payment: Payment) =
        viewModelScope.launch { repository.updatePaymentMethod(payment) }

    fun verifyLogin() = repository.checkLogin()

    data class MessageSummary(val timestamp: Long, val from: String, val content: String)

    fun createMessageSummaryList(list: List<Message>): List<MessageSummary> {

        val messagesVM = ArrayList<MessageSummary>()

        var messageSummary: MessageSummary

        for (message in list) {
            messageSummary = MessageSummary(
                message.timeStamp,
                if (message.sent) "Me" else message.providerId, //TODO: get randomProvider name
                if (message.content.length < 60) message.content else message.content.substring(0..60) + "..."
            )
            messagesVM.add(messageSummary)
        }
        return messagesVM
    }

    fun newMessage(message: Message) =
        viewModelScope.launch { repository.createMessage(message) }

    fun addPen(pen: Pen) =
        viewModelScope.launch { repository.addPen(pen) }

    fun changeProvider(providerId: String, userId: String) =
        viewModelScope.launch { repository.changeProvider(providerId, userId) }

    fun updateDose(dose: Dose) =
        viewModelScope.launch { repository.updateDose(dose) }

    fun removeScheduledDose(dose: Dose) =
        viewModelScope.launch { repository.deleteDose(dose) }

    fun resetDb() = viewModelScope.launch { repository.resetDb() }

    private fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
    }

    fun createUserDataChanged(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirm: String
    ) {
        when {
            !isNameValid(firstName) ->
                _createUserForm.value =
                    CreateUserFormState(firstNameError = R.string.invalid_first_name)
            !isNameValid(lastName) ->
                _createUserForm.value =
                    CreateUserFormState(lastNameError = R.string.invalid_last_name)
            !isEmailValid(email) ->
                _createUserForm.value =
                    CreateUserFormState(emailError = R.string.invalid_email)
            !isPasswordValid(password) ->
                _createUserForm.value =
                    CreateUserFormState(passwordError = R.string.invalid_password)
            !isConfirmValid(password, confirm) ->
                _createUserForm.value =
                    CreateUserFormState(confirmError = R.string.invalid_confirm)
            else ->
                _createUserForm.value = CreateUserFormState(isDataValid = true)
        }
    }

    fun createUser(firstName: String, lastName: String, email: String, password: String) =
        viewModelScope.launch {
            val user =
                User(email = email, firstName = firstName, lastName = lastName, password = HashUtils.sha512(password))
            withContext(IO) {
                _createUserResult.postValue(repository.createUser(user))
            }
        }

}
