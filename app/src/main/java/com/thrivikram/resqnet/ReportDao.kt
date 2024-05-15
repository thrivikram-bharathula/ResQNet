package com.thrivikram.resqnet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Update
    suspend fun updateStatus(report: ReportEntity)

    @Query("SELECT * FROM report_table WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: String): ReportEntity?

    @Query("UPDATE report_table SET image_path = :imagePath WHERE id = :id")
    suspend fun updateImagePath(id: String, imagePath: String)

    @Query("SELECT * FROM report_table WHERE aadhaar = :aadhaar")
    suspend fun getReportsByAadhaar(aadhaar: String): List<ReportEntity>

    @Delete
    suspend fun deleteReport(report: ReportEntity)
}
