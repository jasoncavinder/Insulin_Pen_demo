package com.jasoncavinder.inpen.demo.data.entities.provider

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class ProviderDao :
    BaseDao<Provider> {
    @Query("SELECT * FROM providers")
    abstract fun getData(): List<Provider>

    @Query("SELECT * FROM providers WHERE provider_id = :id LIMIT 1")
    abstract fun getProviderById(id: String): LiveData<Provider>
}


