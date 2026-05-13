package com.suraj.weathersnap.data.local

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherData(
    val city: String = "",
    val country: String = "",
    val condition: String = "",
    val temperature: Int = 0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val pressure: Int = 0
)

data class SavedReport(
    val id: Int = 0,
    val city: String = "",
    val country: String = "",
    val condition: String = "",
    val temperature: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val originalSizeKb: Int = 0,
    val compressedSizeKb: Int = 0,
    val reportNotes: String = "",
    val imagePath: String= ""
)

data class PhotoProperties(
    val imagePath: String,
    val originalKb : Int,
    val compressedKb : Int
)

data class GeocodingResponse(
    val results: List<CityResult>?  // nullable — API returns null if no results
)

data class CityResult(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)

enum class CameraState {
    IDEAL,
    PROCESSING
}

data class SearchSuggestion(
    val name: String
)

// For weather response
data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")       val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("wind_speed_10m")       val windSpeed: Double,
    @SerializedName("surface_pressure")     val pressure: Double,
    @SerializedName("weather_code")         val weatherCode: Int
)

