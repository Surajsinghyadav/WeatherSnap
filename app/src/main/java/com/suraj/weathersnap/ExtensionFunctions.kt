package com.suraj.weathersnap

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.toWeatherCondition(): String {
    return when (this) {
        0         -> "Clear Sky"
        1         -> "Mainly Clear"
        2         -> "Partly Cloudy"
        3         -> "Overcast"
        45, 48    -> "Foggy"
        51, 53    -> "Light Drizzle"
        55        -> "Heavy Drizzle"
        61, 63    -> "Rainy"
        65        -> "Heavy Rain"
        71, 73    -> "Light Snow"
        75        -> "Heavy Snow"
        77        -> "Snow Grains"
        80, 81    -> "Rain Showers"
        82        -> "Heavy Showers"
        85, 86    -> "Snow Showers"
        95        -> "Thunderstorm"
        96, 99    -> "Thunderstorm & Hail"
        else      -> "Unknown"
    }
}

fun Long.toDateString() : String{
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(Date(this))
}