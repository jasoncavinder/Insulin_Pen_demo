/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.user

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import java.util.*


@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(entity = Provider::class, parentColumns = ["id"], childColumns = ["providerId"])
    ],
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["id"], unique = true),
        Index(value = ["providerId"], unique = false)
    ]

)
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id") var userId: String = UUID.randomUUID().toString(),
    var created: Calendar = Calendar.getInstance(),
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String,
    var locationCity: String = "",
    var locationState: String = "",
    var providerId: String? = null,
    var paymentConfigured: Boolean = false
) {
    var location = when (locationCity.isBlank()) {
        true -> ""
        false -> locationCity.plus(", ")
    }.plus(locationState)
}