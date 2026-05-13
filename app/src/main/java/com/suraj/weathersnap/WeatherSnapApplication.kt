package com.suraj.weathersnap

import android.app.Application
import com.suraj.weathersnap.koin.modules.appModules
import com.suraj.weathersnap.koin.modules.databaseModule
import com.suraj.weathersnap.koin.modules.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherSnapApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherSnapApplication)
            modules(
                databaseModule,
                networkModule,
                appModules

            )
        }
    }
}