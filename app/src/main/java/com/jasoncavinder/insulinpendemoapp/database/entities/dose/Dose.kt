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
    @ColumnInfo(name = "id") val doseId: Long? = null,
    val createdTime: Calendar? = Calendar.getInstance(),
    val userId: String,
    val penId: String? = null,
    val type: DoseType,
    val scheduledTime: Calendar? = null,
    val scheduledAmount: Float? = null,
    val givenTime: Calendar? = null,
    val givenAmount: Float? = null
)

enum class DoseType(val intVal: Int) {
    BASAL(0), BOLUS(1);

    companion object {
        private val map by lazy { values().associateBy(DoseType::intVal) }
        operator fun invoke(type: Int) = map.getOrDefault(type, BASAL)
    }
}
