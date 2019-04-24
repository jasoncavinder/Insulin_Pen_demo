package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class AlertDao : BaseDao<Alert> {
    @Query("SELECT * FROM alerts")
    abstract fun getData(): List<Alert>
}


