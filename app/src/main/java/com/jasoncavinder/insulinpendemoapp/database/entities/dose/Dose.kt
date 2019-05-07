/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.dose

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import java.util.*

@Entity(
    tableName = "doses",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Pen::class, parentColumns = ["id"], childColumns = ["penId"])
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["penId"]),
        Index(value = ["userId"])
    ]
)
data class Dose(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val doseID: Long,
    val createdTime: Calendar?,
    val userId: String?,
    val penId: String?,
    val scheduledTime: Calendar?,
    val scheduledAmount: Float?,
    val givenTime: Calendar?,
    val givenAmount: Float?
)