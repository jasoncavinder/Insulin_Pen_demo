/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.old_inpen.demo.replaced.data

//package com.jasoncavinder.inpen.demo.data
//
//import com.jasoncavinder.insulinpendemoapp.database.entities.alert.AlertDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.penPoints.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.penPoints.PenDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
//
//class UserProfileRepository private constructor(
//    private val _userDao: UserDao,
//    private val _providerDao: ProviderDao,
//    private val _penDao: PenDao,
//    private val _penDataPointDao: PenDataPointDao,
//    private val _doseDao: DoseDao,
//    private val _messageDao: MessageDao,
//    private val _alertDao: AlertDao
//) {
//    private var userID: String? = null
//
//    private var providerID: String? = null
//
//    private var penID: String? = null
//
//    var user: User? = null
//        private set
//
//    var provider: Provider? = null
//        private set
//
//    var penPoints: Pen? = null
//        private set
//
//    init {
//        userID = null
//        user = null
//        providerID = null
//        provider = null
//        penID = null
//        penPoints = null
//    }
//
//    fun loadProfile(userID: String) {
//        loadUser(userID)
//        user?.providerID?.let { loadProfile(it) }
//    }
//
//    fun loadUser(userID: String) {
//        user = _userDao.getUserById(userID).value?.apply { password = "" }
//    }
//
//    fun loadProvider(providerID: String) {
//        provider = user?.providerID?.let{_providerDao.getProviderById(it).value}
//    }
//
////    fun loadPen(penID: String) {
////        penPoints = user?.penID?.let{_penDao.getPenById(it).value}
////    }
//
//    companion object {
//
//        @Volatile
//        private var instance: UserProfileRepository? = null
//
//        fun getInstance(
//            userDao: UserDao, providerDao: ProviderDao, penDao: PenDao
//        ) = instance ?: synchronized(this) {
//            instance ?: UserProfileRepository(
//                userDao, providerDao, penDao
//            ).also { instance = it }
//        }
//    }
//}