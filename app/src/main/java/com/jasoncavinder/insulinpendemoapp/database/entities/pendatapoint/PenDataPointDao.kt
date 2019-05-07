package com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface PenDataPointDao : BaseDao<PenDataPoint> {

    @Query("SELECT * FROM pen_data_points WHERE penID = :penID")
    fun getDataPoints(penID: String): LiveData<List<PenDataPoint>>

    @Query("SELECT * FROM pen_data_points")
    fun getAllDataPoints(): LiveData<List<PenDataPoint>>

}
