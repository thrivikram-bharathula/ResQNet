package com.thrivikram.resqnet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_table")
data class ReportEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_path") val imagePath: String?,
    @ColumnInfo(name = "aadhaar") val aadhaar: String,
    @ColumnInfo(name = "status") val status: Int
)
