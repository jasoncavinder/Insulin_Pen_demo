/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.pen

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface PenDao : BaseDao<Pen> {

    @Query("SELECT * FROM pens WHERE id = :id")
    fun getPenById(id: String): Pen?

    @Query("SELECT * FROM pens")
    fun getAllPens(): LiveData<List<Pen>>

    @Query("SELECT * FROM pens WHERE userId = :userId")
    fun getPensByUser(userId: String): LiveData<List<Pen>>

    @Query("SELECT id FROM pens WHERE rowId = :rowId")
    fun getPenByRowId(rowId: Long): String

    @Transaction
    @Query("SELECT * FROM pens WHERE id = :penId")
    fun getPenWithData(penId: String): LiveData<PenWithDataPoints>

    @Transaction
    fun addPen(pen: Pen): String {
        return getPenByRowId(insert(pen))
    }

    @Query("DELETE FROM pens")
    fun deleteAllPens()
}
