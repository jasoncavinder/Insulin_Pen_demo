package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class ProviderDao : BaseDao<Provider> {
    @Query("SELECT * FROM providers")
    abstract fun getData(): List<Provider>
}


