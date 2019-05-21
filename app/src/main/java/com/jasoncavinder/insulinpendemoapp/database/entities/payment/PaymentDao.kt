/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.payment

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface PaymentDao : BaseDao<Payment> {

    @Query("SELECT * FROM payments WHERE userId = :userId LIMIT 1")
    fun getUserPayment(userId: String): LiveData<Payment>

    @Query("DELETE FROM payments")
    fun deleteAllPayments()

}
