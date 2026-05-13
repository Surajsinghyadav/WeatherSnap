package com.suraj.weathersnap.data.repository

import com.suraj.weathersnap.data.local.CityResult
import com.suraj.weathersnap.data.local.WeatherResponse
import com.suraj.weathersnap.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository{
    suspend fun searchCities(query: String): List<CityResult> =
        withContext(Dispatchers.IO) {
            RetrofitClient.geocodingApi.searchCities(query).results ?: emptyList()
        }

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse =
        withContext(Dispatchers.IO) {
            RetrofitClient.weatherApi.getWeather(lat, lon)
        }
}