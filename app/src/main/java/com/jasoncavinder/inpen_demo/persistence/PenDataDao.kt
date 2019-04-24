package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class PenDataDao : BaseDao<PenData> {
    @Query("SELECT * FROM pen_data_points")
    abstract fun getData(): List<PenData>
}


