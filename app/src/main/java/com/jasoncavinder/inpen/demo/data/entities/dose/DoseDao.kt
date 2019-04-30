package com.jasoncavinder.inpen.demo.data.entities.dose

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class DoseDao :
    BaseDao<Dose> {
    @Query("SELECT * FROM doses WHERE dose_id = :doseID OR pen_id = :penID")
    abstract fun getData(doseID: Int = 0, penID: String = ""): List<Dose>
}


