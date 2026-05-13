package com.suraj.weathersnap.data.remote

import com.suraj.weathersnap.data.local.GeocodingResponse
import com.suraj.weathersnap.data.local.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("v1/search")
    suspend fun searchCities(
        @Query("name")  query: String,
        @Query("count") count: Int = 10
    ): GeocodingResponse
}

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude")  lat: Double,
        @Query("longitude") lon: Double,
        @Query("current")   fields: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,surface_pressure,weather_code",
        @Query("timezone")  timezone: String = "auto"
    ): WeatherResponse
}