/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.dose

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface DoseDao : BaseDao<Dose> {

    @Query("SELECT * FROM doses WHERE id = :doseId")
    fun getDose(doseId: Long): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE userId = :userId")
    fun getUserDoses(userId: String? = ""): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE penId = :penId")
    fun getPenDoses(penId: String = ""): LiveData<List<Dose>>

    @Query("SELECT * FROM doses WHERE id = :doseId")
    fun getDoseOnce(doseId: Long): Dose?

    @Query("SELECT * FROM doses WHERE userId = :userId AND penId = :penId")
    fun getRelevantDoses(userId: String? = "", penId: String = ""): LiveData<List<Dose>>

    @Transaction
    fun inject(dose: Dose): Dose? {
        return if (update(dose) > 0) getDoseOnce(dose.doseId!!) else null

    }


}


