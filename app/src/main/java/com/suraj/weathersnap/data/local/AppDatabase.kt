package com.suraj.weathersnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReportsEntity::class, CityResultEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase(){

    abstract fun reportsDao() : ReportsDao
    abstract fun cityResultsDao() : CityResultsDao

}