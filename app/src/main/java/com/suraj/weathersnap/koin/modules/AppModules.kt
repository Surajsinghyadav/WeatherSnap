package com.suraj.weathersnap.koin.modules

import com.suraj.weathersnap.Presentation.WeatherViewModel
import com.suraj.weathersnap.data.repository.WeatherRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val appModules = module {
    single {
        WeatherRepository(
            geocodingApi = get(),
            weatherApi = get(),
            cityResultsDao = get(),
        )
    }

    viewModel {
        WeatherViewModel(
            get(),
            get(),
            get(),
        )
    }


}