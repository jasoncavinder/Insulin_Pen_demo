/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.provider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "providers",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class Provider(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val providerId: String = UUID.randomUUID().toString(),
    val name: String,
    val position: String? = null,
    val rating: Float = 0f,
    val intro: String? = null,
    val copay: Float? = null,
    val languages: List<String>? = null,
    val education: String? = null
)
