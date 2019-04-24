package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class DoseDao : BaseDao<Dose> {
    @Query("SELECT * FROM doses")
    abstract fun getData(): List<Dose>
}


