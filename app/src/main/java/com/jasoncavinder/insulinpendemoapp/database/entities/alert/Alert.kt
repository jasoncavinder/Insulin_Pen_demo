/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.alert

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import java.util.*

@Entity(
    tableName = "alerts",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Pen::class, parentColumns = ["id"], childColumns = ["penId"]),
        ForeignKey(entity = Dose::class, parentColumns = ["id"], childColumns = ["doseId"]),
        ForeignKey(entity = Message::class, parentColumns = ["id"], childColumns = ["messageId"])
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["userId"]),
        Index(value = ["penId"]),
        Index(value = ["doseId"]),
        Index(value = ["messageId"])
    ]
)
data class Alert(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val alertId: Int,
    val userId: String,
    val penId: String? = null,
    val doseId: Int? = null,
    val messageId: Int? = null,
    val created: Calendar = Calendar.getInstance(),
    val summary: String = "",
    val acknowledged: Boolean,
    val acknowledgedTime: Calendar? = null,
    val cleared: Boolean,
    val clearedTime: Calendar? = null
)
