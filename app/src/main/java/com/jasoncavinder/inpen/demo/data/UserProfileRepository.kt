package com.jasoncavinder.inpen.demo.data

import androidx.lifecycle.Transformations
import com.jasoncavinder.inpen.demo.data.entities.alert.AlertDao
import com.jasoncavinder.inpen.demo.data.entities.dose.DoseDao
import com.jasoncavinder.inpen.demo.data.entities.message.MessageDao
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import com.jasoncavinder.inpen.demo.data.entities.pen.PenDao
import com.jasoncavinder.inpen.demo.data.entities.pendata.PenDataDao
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import com.jasoncavinder.inpen.demo.data.entities.provider.ProviderDao
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.data.entities.user.UserDao

class UserProfileRepository private constructor(
    private val _userDao: UserDao,
    private val _providerDao: ProviderDao,
    private val _penDao: PenDao,
    private val _penDataDao: PenDataDao,
    private val _messageDao: MessageDao,
    private val _doseDao: DoseDao,
    private val _alertDao: AlertDao
) {
    private var userID: String? = null

    private var providerID: String? = null

    private var penID: String? = null

    var user: User? = null
        private set

    var provider: Provider? = null
        private set

    var pen: Pen? = null
        private set

    init {
        userID = null
        user = null
        providerID = null
        provider = null
        penID = null
        pen = null
    }

    fun loadProfile(userID: String) {
        loadUser(userID)
        user?.providerID?.let { loadProfile(it) }
        user?.penID?.let{loadPen(it)}
    }

    fun loadUser(userID: String) {
        user = _userDao.getUser(userID).value?.apply { password = "" }
    }

    fun loadProvider(providerID: String) {
        provider = user?.providerID?.let{_providerDao.getProviderById(it).value}
    }

    fun loadPen(penID: String) {
        pen = user?.penID?.let{_penDao.getPenById(it).value}
    }

    companion object {

        @Volatile
        private var instance: UserProfileRepository? = null

        fun getInstance(
            userDao: UserDao, providerDao: ProviderDao, penDao: PenDao,
            penDataDao: PenDataDao, messageDao: MessageDao, doseDao: DoseDao,
            alertDao: AlertDao
        ) = instance ?: synchronized(this) {
            instance ?: UserProfileRepository(
                userDao, providerDao, penDao, penDataDao, messageDao, doseDao, alertDao
            ).also { instance = it }
        }

    }
}