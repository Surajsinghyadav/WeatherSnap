package com.suraj.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityResultEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)