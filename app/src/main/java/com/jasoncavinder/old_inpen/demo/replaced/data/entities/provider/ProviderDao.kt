//package com.jasoncavinder.inpen.demo.replaced.data.entities.provider
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Query
//import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
//
//@Dao
//abstract class ProviderDao : BaseDao<Provider> {
//    @Query("SELECT * FROM providers")
//    abstract fun getProviders(): List<Provider>
//
//    @Query("SELECT * FROM providers WHERE id = :id LIMIT 1")
//    abstract fun getProviderById(id: String): LiveData<Provider>
//}
//
//
