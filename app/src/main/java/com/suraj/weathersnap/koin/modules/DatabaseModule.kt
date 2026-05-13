package com.suraj.weathersnap.koin.modules

import androidx.room.Room
import com.suraj.weathersnap.data.local.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "WeatherSnap"
        ).fallbackToDestructiveMigration(true)
         .build()
    }

    single {
        get<AppDatabase>().reportsDao()
    }

    single {
        get<AppDatabase>().cityResultsDao()
    }


}