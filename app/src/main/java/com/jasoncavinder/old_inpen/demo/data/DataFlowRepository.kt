/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.old_inpen.demo.data

//package com.jasoncavinder.inpen.demo.data
//
//import com.jasoncavinder.insulinpendemoapp.database.entities.alert.Alert
//import com.jasoncavinder.insulinpendemoapp.database.entities.alert.AlertDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
//import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
//import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPoint
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class DataFlowRepository private constructor(
//    private val _penDataPointDao: PenDataPointDao,
//    private val _doseDao: DoseDao,
//    private val _messageDao: MessageDao,
//    private val _alertDao: AlertDao
//) {
////    private var userID: String? = null
////    private var providerID: String? = null
////    private var penID: String? = null
//
//    var penDataPoints: List<PenDataPoint>? = null
//        private set
//    var doses: List<Dose>? = null
//        private set
//    var messages: List<Message>? = null
//        private set
//    var alerts: List<Alert>? = null
//        private set
//
//    init {
////        userID = null
////        providerID = null
////        penID = null
//        penDataPoints = null
//        doses = null
//        messages = null
//        alerts = null
//        loadAlerts()
//    }
//
//    fun loadAlerts() = GlobalScope.launch {
//        alerts = _alertDao.getDataPoints()  // get all alerts
//            // filter out irrelevant datapoint alerts
//            .filter { alert -> penDataPoints?.any { it.penID == alert.penID } ?: true }
//            // filter out irrelevant dose alerts
//            .filter { alert -> doses?.any { it.doseID == alert.doseID } ?: true }
//            // filter out irrelevant message alerts
//            .filter { alert -> messages?.any { it.messageID == alert.messageID } ?: true }
//    }
//
//    fun loadDataPoints(penID: String) = _penDataPointDao.getDataPoints(penID)
//
//    fun loadMessages(userID: String) = _messageDao.getDataPoints(userID)
//
//    fun loadUserDoses(userID: String) = _doseDao.getDataPoints(userID = userID)
//
//    fun loadPenDoses(penID: String) = _doseDao.getDataPoints(penID = penID)
//
//    fun loadDose(doseID: Int) = _doseDao.getDataPoints(doseID = doseID)
//
//    companion object {
//
//        @Volatile
//        private var instance: DataFlowRepository? = null
//
//        fun getInstance(
//            penDataPointDao: PenDataPointDao, doseDao: DoseDao, messageDao: MessageDao, alertDao: AlertDao
//        ) = instance ?: synchronized(this) {
//            instance ?: DataFlowRepository(
//                penDataPointDao, doseDao, messageDao, alertDao
//            ).also { instance = it }
//        }
//    }
//
//}
