package com.jasoncavinder.insulinpendemoapp.database.entities.alert

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface AlertDao : BaseDao<Alert> {

    @Query("SELECT * FROM alerts")
    fun getAll(): LiveData<List<Alert>>

    @Query("SELECT * FROM alerts WHERE userId = :userId")
    fun getUserAlerts(userId: String): LiveData<List<Alert>>

}


