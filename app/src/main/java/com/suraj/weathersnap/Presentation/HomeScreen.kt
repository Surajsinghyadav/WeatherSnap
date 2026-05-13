package com.suraj.weathersnap.Presentation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suraj.weathersnap.Presentation.SearchUiState.*
import com.suraj.weathersnap.ui.theme.AccentGreen
import com.suraj.weathersnap.ui.theme.CardBackground
import com.suraj.weathersnap.ui.theme.ChipBackground
import com.suraj.weathersnap.ui.theme.DarkBackground
import com.suraj.weathersnap.ui.theme.DarkButtonBg
import com.suraj.weathersnap.ui.theme.DividerColor
import com.suraj.weathersnap.ui.theme.HeaderGreenDark
import com.suraj.weathersnap.ui.theme.HeaderGreenLight
import com.suraj.weathersnap.ui.theme.HumidityBlue
import com.suraj.weathersnap.ui.theme.InputBorder
import com.suraj.weathersnap.ui.theme.PressureOrange
import com.suraj.weathersnap.ui.theme.TempOrange
import com.suraj.weathersnap.ui.theme.TextMuted
import com.suraj.weathersnap.ui.theme.TextPrimary
import com.suraj.weathersnap.ui.theme.TextSecondary
import com.suraj.weathersnap.ui.theme.WindTeal
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: WeatherViewModel = koinViewModel(),
    onNavigateToReports: () -> Unit,
    onNavigateToCreateReport: () -> Unit,
    padding: PaddingValues
) {
    val searchState by viewModel.searchUiState.collectAsState()
    val weatherCardState by viewModel.weatherCardUiState.collectAsState()
    val cityQuery by viewModel.cityQuery.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val state = searchState
    val weatherState = weatherCardState
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(HeaderGreenLight, HeaderGreenDark)),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(padding)
                .padding(horizontal = 10.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "WeatherSnap", color = Color(0xFF1A2205),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Live weather reports with camera evidence",
                    fontSize = 11.sp,
                    color = Color(0xFF3A4F10)
                )
            }
            TextButton(
                onClick = onNavigateToReports,
                modifier = Modifier
                    .width(80.dp)
                    .background(DarkButtonBg, RoundedCornerShape(30.dp))
            ) {
                Text(
                    "Reports",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Search ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBackground, RoundedCornerShape(12.dp))
                        .border(1.dp, InputBorder, RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = cityQuery,
                        onValueChange = { viewModel.onCityQueryChange(it) },
//                        placeholder = { Text("Enter city...", color = TextMuted) },
                        label = { Text("City", color = TextSecondary, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = AccentGreen
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
//                            viewModel.onSearch()
                        })
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(HeaderGreenLight)
                            .clickable {
                                viewModel.clearSuggestions()
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            "Clear",
                            color = Color(0xFF1A2205),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                Text(
                    "Enter more than 2 letters to start city suggestions.",
                    color = TextMuted, fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )



                when (state) {

                    Ideal -> {}
                    Loading -> {
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Error -> {
                        Text(
                            text = state.e,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    is Success -> {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 250.dp)
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                items(state.cityResult) {
                                    val name = "${it.name}, ${it.country}"
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.onSearch(
                                                it.latitude,
                                                it.longitude,
                                                it.name,
                                                it.country
                                            )

                                        },
                                        border = BorderStroke(
                                            1.dp,
                                            color = Color.White.copy(alpha = .3f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color.White.copy(.7f)
                                        )
                                    ) {
                                        Log.d("errorCheck", "Search Sugg $it")
                                        Text(
                                            text = name,
                                            fontSize = 14.sp,
                                        )
                                    }


                                }
                            }
                        }

                    }

                }

            }

            Spacer(Modifier.height(16.dp))

            WeatherCard(weatherState, modifier = Modifier.padding(horizontal = 16.dp)) {
                onNavigateToCreateReport()
            }

        }


    }


}

// ── Shared Composables ───────────────────────────────────────────

@Composable
fun WeatherCard(
    weatherCardUiState: WeatherCardUiState,
    modifier: Modifier = Modifier,
    onCreateReport: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        when (weatherCardUiState) {
            is WeatherCardUiState.Ideal -> {
//               val gradient = Brush.

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color(0xD3434B09),
                                        Color(0xD7224F44)
                                    ),
                                    startX = 0f,
                                    endX = 530f
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            "Search. Capture. Save.",
                            fontSize = 13.sp,
                            color = Color.White.copy(.7f)
                        )


                    }
                    Spacer(Modifier.height(8.dp))

                    Text(
                        "No weather loaded",
                        fontSize = 13.sp,
                        color = Color.White.copy(.7f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Enter more than 2 letters, choose a city, then search.",
                        fontSize = 13.sp,
                        color = Color.White.copy(.7f)
                    )

                }


            }

            is WeatherCardUiState.Loading -> {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()

                }
            }

            is WeatherCardUiState.Success -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            "${weatherCardUiState.weatherData.city}, ${weatherCardUiState.weatherData.country}",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            weatherCardUiState.weatherData.condition,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4A5A1A))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "${weatherCardUiState.weatherData.temperature}°C",
                            color = TempOrange,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricChip(
                        "Humidity",
                        "${weatherCardUiState.weatherData.humidity}%",
                        HumidityBlue,
                        Modifier.weight(1f)
                    )
                    MetricChip(
                        "Wind",
                        "${weatherCardUiState.weatherData.windSpeed} m/s",
                        WindTeal,
                        Modifier.weight(1f)
                    )
                    MetricChip(
                        "Pressure",
                        "${weatherCardUiState.weatherData.pressure}",
                        PressureOrange,
                        Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Report readiness", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        "Camera and Room DB enabled",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(HeaderGreenLight)
                        .clickable { onCreateReport() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Create Report",
                        color = Color(0xFF1A2205),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            is WeatherCardUiState.Error -> {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = weatherCardUiState.error,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.error

                    )

                }

            }

        }
    }
}

@Composable
fun MetricChip(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(ChipBackground, RoundedCornerShape(8.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(label, color = TextMuted, fontSize = 10.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}