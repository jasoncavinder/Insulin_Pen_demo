/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.payment

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import java.util.*

const val VISA = 0
const val MASTER = 1
const val PAYPAL = 2
const val AMAZON = 3

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ],
    indices = [
        Index(value = ["userId"], unique = true)
    ]

)
data class Payment(
    @PrimaryKey
    @ColumnInfo(name = "id") var paymentId: String = UUID.randomUUID().toString(),
    var userId: String,
    var type: Int, // one of VISA, MASTER, PAYPAL, AMAZON
    var ccnum: Long?,
    var ccexp: Int?,
    var ccname: String?,
    var email: String?
)
