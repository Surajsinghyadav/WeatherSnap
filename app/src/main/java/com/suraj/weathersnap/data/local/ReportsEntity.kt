package com.suraj.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReportsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String = "",
    val country: String = "",
    val condition: String = "",
    val temperature: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val originalSizeKb: Int = 0,
    val compressedSizeKb: Int = 0,
    val reportNotes: String = "",
    val imagePath: String = ""
)