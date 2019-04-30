package com.jasoncavinder.inpen.demo.data.entities.dose

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class DoseDao :
    BaseDao<Dose> {
    @Query("SELECT * FROM doses WHERE user_id = :userID OR pen_id = :penID OR dose_id = :doseID")
    abstract fun getData(userID: String? = "", penID: String = "", doseID: Int = 0): List<Dose>
}


