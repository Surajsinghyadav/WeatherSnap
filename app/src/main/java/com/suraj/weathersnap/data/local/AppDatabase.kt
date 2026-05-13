package com.suraj.weathersnap.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReportsEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    abstract fun reportsDao() : ReportsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Weather_Snap"
                ).apply {
                    fallbackToDestructiveMigration(true)
                }.build()
                INSTANCE = instance
                return instance
            }
        }
    }


}