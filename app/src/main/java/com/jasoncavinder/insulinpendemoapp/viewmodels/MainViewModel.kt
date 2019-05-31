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
import kotlin.math.roundToInt


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

    /* Repository */
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


    /* State, Private  */
    private val _loginFormState = MutableLiveData<LoginFormState>()
    private val _createUserFormState = MutableLiveData<CreateUserFormState>()


    /* State, Public */
    val loginFormState: LiveData<LoginFormState> = _loginFormState
    val createUserFormState: LiveData<CreateUserFormState> = _createUserFormState
    val touchLoginAvailable = repository.touchLoginAvailable


    /* Asynchronous results, Private */
    private val _createUserResult = MutableLiveData<Result<String>>()


    /* Asynchronous Results, Public */
    val loginResult: LiveData<Result<String>> = repository.loginResult
    val createUserResult: LiveData<Result<String>> = _createUserResult
    val updateUserResult: LiveData<Result<User>> = repository.updateUserResult
    val updatePaymentResult: LiveData<Result<Payment>> = repository.updatePaymentResult
    val addPenResult: LiveData<Result<Pen>> = repository.addPenResult
    val changeProviderResult: LiveData<Result<Provider>> = repository.changeProviderResult
    val changePenResult: LiveData<Result<PenWithDataPoints>> = repository.changePenResult
    val editDoseResult: LiveData<Result<Dose>> = repository.editDoseResult
    val injectDoseResult: LiveData<Result<Dose>> = repository.injectDoseResult


    /* Private Data */
    private val _providerList = repository.getProviders()
    private val _userId: LiveData<String> =
        Transformations.switchMap(repository.userIdLiveData) { MutableLiveData<String>(it) }
    private val _userProfile: LiveData<UserProfile> =
        Transformations.switchMap(_userId) { repository.loadUserProfile(it) }


    /* Public Data */
    val localUsers: LiveData<Int> = repository.getLocalUserCount()
    val userId: LiveData<String> = _userId
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
    val randomProvider: LiveData<Provider> =
        Transformations.switchMap(_providerList) {
            MutableLiveData<Provider>(
                if (it.isNotEmpty()) it.shuffled().first() else throw Exception(
                    "No providers were found. This usually you didn't uninstall and relaunch after a version upgrade."
                )
            )
        }
    val user: MediatorLiveData<User> = MediatorLiveData()
    val paymentMethod: MediatorLiveData<Payment> = MediatorLiveData()
    val provider: MediatorLiveData<Provider> = MediatorLiveData()
    val pen: MediatorLiveData<PenWithDataPoints> = MediatorLiveData()
    val penStatus = PenStatus()
    val messages: LiveData<List<Message>> =
        Transformations.switchMap(userId) { repository.loadMessages(it) }
    val unreadMessages: LiveData<List<MessageSummary>> =
        Transformations.map(messages) { messageList -> createMessageSummaryList(messageList) }
    val noMessages: LiveData<Boolean> =
        Transformations.map(unreadMessages) { unreadList -> unreadList.isEmpty() }
    val doses: LiveData<List<Dose>> =
        Transformations.switchMap(userId) { repository.getUserDoses(it) }
    val nextBasalDose: LiveData<Dose> =
        Transformations.switchMap(doses) { doseList ->
            MutableLiveData<Dose>(doseList.sortedByDescending { it.scheduledTime }
                .firstOrNull { (it.type == DoseType.BASAL && it.scheduledTime?.after(Calendar.getInstance()) ?: false) })
        }
    val nextBolusDose: LiveData<Dose> =
        Transformations.switchMap(doses) { doseList ->
            MutableLiveData<Dose>(doseList.sortedByDescending { dose -> dose.createdTime }
                .firstOrNull { it.type == DoseType.BOLUS && it.givenTime == null })
        }

//    var nextBasalDose: MediatorLiveData<Dose> = MediatorLiveData()
//    var nextBolusDose: MediatorLiveData<Dose> = MediatorLiveData()

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
//        nextBasalDose.addSource(doses) {
//            nextBasalDose.value = it
//                .sortedByDescending { it.scheduledTime }
//                .firstOrNull { dose ->
//                    (dose.type == DoseType.BASAL && dose.scheduledTime?.after(Calendar.getInstance()) ?: false)
//                }
//        }
//        nextBolusDose.addSource(doses) {
//            nextBolusDose.value = it.sortedByDescending { dose -> dose.createdTime }
//                .firstOrNull { dose -> dose.type == DoseType.BOLUS }
//        }

//        try {
//
//        } catch (ex: java.lang.Exception) {
//            Log.d(TAG, "Failed loading demoMessages", ex)
//        }
    }

    fun login(email: String = "", password: String = "", touch: Boolean = false) {
        viewModelScope.launch {
            when {
                touch -> repository.touchLoginSim()
                else -> repository.login(email, HashUtils.sha512(password))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginFormState.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
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

    data class MessageSummary(val timestamp: Calendar, val from: String, val content: String)

    fun createMessageSummaryList(list: List<Message>): List<MessageSummary> {

        val messagesVM = ArrayList<MessageSummary>()

        for (message in list) {
            if (!message.read)
                messagesVM.add(
                    MessageSummary(
                        message.timeStamp,
                        message.providerId,
                        if (message.content.length < 60) message.content else message.content.substring(0..60) + "..."
                    )
                )
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
                _createUserFormState.value =
                    CreateUserFormState(firstNameError = R.string.invalid_first_name)
            !isNameValid(lastName) ->
                _createUserFormState.value =
                    CreateUserFormState(lastNameError = R.string.invalid_last_name)
            !isEmailValid(email) ->
                _createUserFormState.value =
                    CreateUserFormState(emailError = R.string.invalid_email)
            !isPasswordValid(password) ->
                _createUserFormState.value =
                    CreateUserFormState(passwordError = R.string.invalid_password)
            !isConfirmValid(password, confirm) ->
                _createUserFormState.value =
                    CreateUserFormState(confirmError = R.string.invalid_confirm)
            else ->
                _createUserFormState.value = CreateUserFormState(isDataValid = true)
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

    fun injectDose(dose: Dose) {
        viewModelScope.launch { repository.injectDose(dose) }
    }

    fun sendMessage(messageContent: String) {
        val userId: String = userId.value ?: run {
            Log.d(TAG, "userId is null!?")
            return
        }
        val providerId: String = provider.value?.providerId ?: run {
            Log.d(TAG, "providerId is null!?")
            return
        }
        val message =
            Message(userId = userId, providerId = providerId, sent = true, content = messageContent, read = true)
        viewModelScope.launch {
            withContext(IO) {
                repository.sendMessage(message)
            }
        }
    }

    fun updateMessage(message: Message) {
        viewModelScope.launch { withContext(IO) { repository.updateMessage(message) } }
    }


    class Dosage(
        val userWeightInPounds: Int = 160,
        val totalDailyInsulinDose: Int = userWeightInPounds.div(4),
        val basalDose: Int = totalDailyInsulinDose.div(2),
        val carbCoverageRatio: Float = 500f.div(totalDailyInsulinDose),
        val correctionFactor: Float = 1800f.div(totalDailyInsulinDose),
        val targetBloodSugar: Int = 120,
        var actualBloodSugar: Int = 0,
        var carbsAtMeal: Int = 0
    ) {

        private val _carbCoverage: Float
            get() = carbsAtMeal.div(carbCoverageRatio)
        private val _excessBloodSugar: Int
            get() = if (actualBloodSugar > targetBloodSugar) actualBloodSugar.minus(targetBloodSugar) else 0
        private val _bloodSugarCoverage: Float
            get() = _excessBloodSugar.div(correctionFactor)

        val carbCoverage: Int
            get() = _carbCoverage.roundToInt()
        val bloodSugarCoverage: Int
            get() = _bloodSugarCoverage.roundToInt()
        val bolusDose: Int
            get() = (_carbCoverage.plus(_bloodSugarCoverage)).roundToInt()

    }
}
