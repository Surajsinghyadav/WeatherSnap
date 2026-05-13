package com.suraj.weathersnap

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherSnapApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherSnapApplication)
            modules(

            )
        }
    }
}