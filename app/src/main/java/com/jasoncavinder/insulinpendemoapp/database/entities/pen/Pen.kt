/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.pen

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User

@Entity(
    tableName = "pens",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["userId"], unique = false)]
)
data class Pen(
    @PrimaryKey
    @ColumnInfo(name = "id") var penId: String,
    var userId: String,
    val name: String? = "My Insulin Pen",
    val type: String? = "Smart Pen",
    val version: String? = "Version 1.0"
)