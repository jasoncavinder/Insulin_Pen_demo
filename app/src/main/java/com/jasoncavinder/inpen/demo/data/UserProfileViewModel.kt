package com.jasoncavinder.inpen.demo.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jasoncavinder.inpen.demo.data.entities.alert.Alert
import com.jasoncavinder.inpen.demo.data.entities.alert.AlertDao
import com.jasoncavinder.inpen.demo.data.entities.dose.Dose
import com.jasoncavinder.inpen.demo.data.entities.dose.DoseDao
import com.jasoncavinder.inpen.demo.data.entities.message.Message
import com.jasoncavinder.inpen.demo.data.entities.message.MessageDao
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import com.jasoncavinder.inpen.demo.data.entities.pen.PenDao
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPoint
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPointDao
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import com.jasoncavinder.inpen.demo.data.entities.provider.ProviderDao
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.data.entities.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val SESSION_TIME_MINUTES: Int = 15

    private var _parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val _coroutineContext: CoroutineContext
        get() = _parentJob + Dispatchers.Main
    private val scope = CoroutineScope(_coroutineContext)

    private var _sessionExpires = 0L
        get() = _sessionExpires.also { _sessionExpires = System.currentTimeMillis() + SESSION_TIME_MINUTES.times(60000L) }

    private fun extendSession() {
        _sessionExpires = System.currentTimeMillis() + SESSION_TIME_MINUTES.times(60000L)
    }

    val userDao: UserDao
    val providerDao: ProviderDao
    val penDao: PenDao
    val penDataPointDao: PenDataPointDao
    val messageDao: MessageDao
    val doseDao: DoseDao
    val alertDao: AlertDao

    private val _user = MutableLiveData<User>()
    //    private val _userMediator = MediatorLiveData<User>()
    val user: LiveData<User> = _user


    private val _provider = MutableLiveData<Provider>()
    val provider: LiveData<Provider> = _provider

    private val _pen = MutableLiveData<Pen>()
    val pen: LiveData<Pen> = _pen

    private val _penDataPoints = MutableLiveData<List<PenDataPoint>>()
    val penDataPoints: LiveData<List<PenDataPoint>> = _penDataPoints

    private val _doses = MutableLiveData<List<Dose>>()
    val doses: LiveData<List<Dose>> = _doses

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _alerts = MutableLiveData<List<Alert>>()
    val alerts: LiveData<List<Alert>> = _alerts

    private val _userProfileRepository: UserProfileRepository

    private val _dataFlowRepository: DataFlowRepository

    init {
        _sessionExpires = 0L
        AppDatabase.getInstance(application)
            .also { userDao = it.userDao() }
            .also { providerDao = it.providerDao() }
            .also { penDao = it.penDao() }
            .also { penDataPointDao = it.penDataPointDao() }
            .also { messageDao = it.messageDao() }
            .also { doseDao = it.doseDao() }
            .also { alertDao = it.alertDao() }

        _userProfileRepository = UserProfileRepository.getInstance(
            userDao, providerDao, penDao
        )

        _dataFlowRepository = DataFlowRepository.getInstance(penDataPointDao, doseDao, messageDao, alertDao)

        _user.value = _userProfileRepository.user
        _provider.value = _userProfileRepository.provider
        _pen.value = _userProfileRepository.pen
        _penDataPoints.value = _dataFlowRepository.penDataPoints
        _doses.value = _dataFlowRepository.doses
        _messages.value = _dataFlowRepository.messages
        _alerts.value = _dataFlowRepository.alerts

        _user.observeForever {
            _user.value?.userID?.let { _dataFlowRepository.loadMessages(it) }
            _user.value?.userID?.let { _dataFlowRepository.loadUserDoses(it) }
        }

        _pen.observeForever {
            _pen.value?.penID?.let { _dataFlowRepository.loadDataPoints(it) }
            _pen.value?.penID?.let { _dataFlowRepository.loadPenDoses(penID = it) }
        }

    }

    fun loadUser(userID: String) {
        _userProfileRepository.loadProfile(userID)
    }

    fun isAuthenticated(): Boolean = _user.value != null && _sessionExpires < System.currentTimeMillis()

    override fun onCleared() {
        super.onCleared()
        _parentJob.cancel()
    }
}
