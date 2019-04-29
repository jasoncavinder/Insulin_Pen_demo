package com.jasoncavinder.inpen.demo.data.entities.pendata

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class PenDataDao :
    BaseDao<PenData> {
    @Query("SELECT * FROM pen_data_points")
    abstract fun getData(): List<PenData>
}


