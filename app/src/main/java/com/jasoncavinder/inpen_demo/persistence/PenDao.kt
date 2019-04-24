package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class PenDao : BaseDao<Pen> {
    @Query("SELECT * FROM pens")
    abstract fun getData(): List<Pen>
}


