package com.suraj.weathersnap.koin

import com.suraj.weathersnap.Presentation.WeatherViewModel
import com.suraj.weathersnap.data.repository.WeatherRepository
import org.koin.dsl.module

val module = module {
    single { WeatherViewModel(get()) }
    single { WeatherRepository() }
}