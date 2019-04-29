package com.jasoncavinder.inpen.demo.data.entities.alert

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class AlertDao :
    BaseDao<Alert> {
    @Query("SELECT * FROM alerts")
    abstract fun getData(): List<Alert>
}


