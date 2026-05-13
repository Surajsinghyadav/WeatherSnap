package com.suraj.weathersnap.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportsDao {

    @Query("SELECT * from ReportsEntity ORDER BY DATE DESC")
    fun getAllReports(): Flow<List<ReportsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReport(report: ReportsEntity)

    @Query("SELECT COUNT(*) FROM ReportsEntity")
    fun getTotalReportCount(): Flow<Int>
}