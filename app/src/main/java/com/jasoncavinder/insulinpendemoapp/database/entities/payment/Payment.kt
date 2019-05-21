/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.payment

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import java.util.*

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = CASCADE)
    ],
    indices = [
        Index(value = ["userId"], unique = true)
    ]
)
data class Payment(
    @PrimaryKey
    @ColumnInfo(name = "id") var paymentId: String = UUID.randomUUID().toString(),
    var userId: String,
    var type: PaymentType,
    var ccnum: Long? = null,
    var ccexp: Int? = null,
    var ccname: String? = null,
    var email: String? = null
)

enum class PaymentType(val intVal: Int) {
    VISA(0), MASTER(1), PAYPAL(10), AMAZON(11);

    companion object {
        private val map by lazy { values().associateBy(PaymentType::intVal) }
        operator fun invoke(type: Int) = map.getOrDefault(type, VISA)
    }
}
