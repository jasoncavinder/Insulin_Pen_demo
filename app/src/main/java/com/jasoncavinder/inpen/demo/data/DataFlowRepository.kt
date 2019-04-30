package com.jasoncavinder.inpen.demo.data

import com.jasoncavinder.inpen.demo.data.entities.alert.Alert
import com.jasoncavinder.inpen.demo.data.entities.alert.AlertDao
import com.jasoncavinder.inpen.demo.data.entities.dose.Dose
import com.jasoncavinder.inpen.demo.data.entities.dose.DoseDao
import com.jasoncavinder.inpen.demo.data.entities.message.Message
import com.jasoncavinder.inpen.demo.data.entities.message.MessageDao
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPoint
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPointDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DataFlowRepository private constructor(
    private val _penDataPointDao: PenDataPointDao,
    private val _doseDao: DoseDao,
    private val _messageDao: MessageDao,
    private val _alertDao: AlertDao
) {
//    private var userID: String? = null
//    private var providerID: String? = null
//    private var penID: String? = null

    var penDataPoints: List<PenDataPoint>? = null
        private set
    var doses: List<Dose>? = null
        private set
    var messages: List<Message>? = null
        private set
    var alerts: List<Alert>? = null
        private set

    init {
//        userID = null
//        providerID = null
//        penID = null
        penDataPoints = null
        doses = null
        messages = null
        alerts = null
        loadAlerts()
    }

    fun loadAlerts() = GlobalScope.launch {
        alerts = _alertDao.getData()  // get all alerts
            // filter out irrelevant datapoint alerts
            .filter { alert -> penDataPoints?.any { it.penID == alert.penID } ?: true }
            // filter out irrelevant dose alerts
            .filter { alert -> doses?.any { it.doseID == alert.doseID } ?: true }
            // filter out irrelevant message alerts
            .filter { alert -> messages?.any { it.messageID == alert.messageID } ?: true }
    }

    fun loadDataPoints(penID: String) = _penDataPointDao.getData(penID)

    fun loadMessages(userID: String) = _messageDao.getData(userID)

    fun loadUserDoses(userID: String) = _doseDao.getData(userID = userID)

    fun loadPenDoses(penID: String) = _doseDao.getData(penID = penID)

    fun loadDose(doseID: Int) = _doseDao.getData(doseID = doseID)

    companion object {

        @Volatile
        private var instance: DataFlowRepository? = null

        fun getInstance(
            penDataPointDao: PenDataPointDao, doseDao: DoseDao, messageDao: MessageDao, alertDao: AlertDao
        ) = instance ?: synchronized(this) {
            instance ?: DataFlowRepository(
                penDataPointDao, doseDao, messageDao, alertDao
            ).also { instance = it }
        }
    }

}
