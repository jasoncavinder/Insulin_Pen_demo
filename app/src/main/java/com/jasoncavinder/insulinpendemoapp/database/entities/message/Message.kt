/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.message

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Provider::class, parentColumns = ["id"], childColumns = ["providerId"])
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["userId"]),
        Index(value = ["userId", "providerId"]),
        Index(value = ["providerId"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val messageId: Int,
    val timeStamp: Long,
    val userId: String,
    val providerId: String,
    val sent: Boolean, // true = from randomProvider, false = to randomProvider
    val content: String,
    val read: Boolean
)