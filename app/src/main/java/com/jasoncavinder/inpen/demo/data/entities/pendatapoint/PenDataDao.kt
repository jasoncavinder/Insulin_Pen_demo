package com.jasoncavinder.inpen.demo.data.entities.pendatapoint

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class PenDataDao :
    BaseDao<PenDataPoint> {
    @Query("SELECT * FROM pen_data_points WHERE pen_id = :penID")
    abstract fun getData(penID: String): List<PenDataPoint>
}
