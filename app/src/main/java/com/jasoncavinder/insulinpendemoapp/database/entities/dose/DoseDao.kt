package com.jasoncavinder.insulinpendemoapp.database.entities.dose

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface DoseDao : BaseDao<Dose> {

    @Query("SELECT * FROM doses WHERE id = :doseId")
    fun getDose(doseId: Int): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE userId = :userId")
    fun getUserDoses(userId: String? = ""): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE penId = :penId")
    fun getPenDoses(penId: String = ""): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE userId = :userId AND penId = :penId")
    fun getRelevantDoses(userId: String? = "", penId: String = ""): LiveData<List<Dose>>

}


