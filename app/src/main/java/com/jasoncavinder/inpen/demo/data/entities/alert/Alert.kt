package com.jasoncavinder.inpen.demo.data.entities.alert

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "alerts",
    indices = [
        Index(value = ["alert_id"], unique = true)
    ]
)
data class Alert(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alert_id") val alert_id: Int,
    @ColumnInfo(name = "ts") val ts: Calendar,
    @ColumnInfo(name = "type") val type: String, // string should match table name of related data
    @ColumnInfo(name = "message_id") val data_id: Int? = null,
    @ColumnInfo(name = "pen_id") val pen_id: String? = null,
    @ColumnInfo(name = "dose_id") val dose_id: Int? = null,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "acknowledged") val acknowledged: Boolean,
    @ColumnInfo(name = "cleared") val cleared: Boolean
) {
    enum class AlertType {
        MESSAGE, DOSE, TEMP, OTHER
    }
}