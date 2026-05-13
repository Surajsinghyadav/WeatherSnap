package com.suraj.weathersnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.suraj.weathersnap.Presentation.WeatherViewModel
import com.suraj.weathersnap.navigation.WeatherSnapNavigation
import com.suraj.weathersnap.ui.theme.WeatherSnapTheme

class MainActivity : ComponentActivity() {
    private val weatherViewModel : WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = 1
            )
        )
        setContent {
            WeatherSnapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherSnapNavigation(
                        weatherViewModel,
                        padding = innerPadding
                    )
                }
            }
        }
    }
}
