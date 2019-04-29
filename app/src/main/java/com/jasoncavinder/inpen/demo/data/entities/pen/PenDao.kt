package com.jasoncavinder.inpen.demo.data.entities.pen

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class PenDao : BaseDao<Pen> {
    @Query("SELECT * FROM pens")
    abstract fun getData(): List<Pen>

    @Query("SELECT * FROM pens WHERE pen_id = :id LIMIT 1")
    abstract fun getPenById(id: String): LiveData<Pen>
}


