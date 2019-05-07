/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.provider

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface ProviderDao : BaseDao<Provider> {

    @Query("SELECT * FROM providers")
    fun getProviders(): LiveData<List<Provider>>

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: String): LiveData<Provider>

}


