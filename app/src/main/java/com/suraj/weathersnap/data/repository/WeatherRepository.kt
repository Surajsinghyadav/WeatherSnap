package com.suraj.weathersnap.data.repository

import com.suraj.weathersnap.data.local.CityResult
import com.suraj.weathersnap.data.local.CityResultsDao
import com.suraj.weathersnap.data.local.WeatherResponse
import com.suraj.weathersnap.data.mapper.toEntity
import com.suraj.weathersnap.data.mapper.toExternalModel
import com.suraj.weathersnap.data.remote.GeocodingApiService
import com.suraj.weathersnap.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val cityResultsDao: CityResultsDao,
    private val geocodingApi: GeocodingApiService,
    private val weatherApi : WeatherApiService
){
    suspend fun searchCities(query: String): List<CityResult> =
        withContext(Dispatchers.IO) {
            val dbResults = cityResultsDao.localSearch(query)
            if (dbResults.isNotEmpty()){
                return@withContext dbResults.map { it.toExternalModel() }
            }

            val remoteResults = geocodingApi.searchCities(query).results ?: emptyList()

            if (remoteResults.isNotEmpty()){
                cityResultsDao.saveCityResults(remoteResults.map { it.toEntity() })
            }
            return@withContext remoteResults

        }

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse =
        withContext(Dispatchers.IO) {
            weatherApi.getWeather(lat, lon)
        }
}